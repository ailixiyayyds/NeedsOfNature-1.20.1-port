#!/usr/bin/env python3
"""Apply Yarn intermediary-to-named identifiers to CFR-decompiled Java sources."""

from __future__ import annotations

import argparse
import re
import shutil
from pathlib import Path


def read_tiny(path: Path) -> tuple[dict[str, str], dict[str, str]]:
    classes: dict[str, str] = {}
    members: dict[str, str] = {}
    with path.open("r", encoding="utf-8") as handle:
        header = handle.readline().rstrip("\n").split("\t")
        if header[:3] != ["tiny", "2", "0"]:
            raise ValueError(f"Unsupported mappings header: {header!r}")
        namespaces = header[3:]
        intermediary = namespaces.index("intermediary")
        named = namespaces.index("named")

        for raw in handle:
            columns = raw.rstrip("\n").split("\t")
            indent = len(columns) - len(raw.lstrip("\t").rstrip("\n").split("\t"))
            data = columns[indent:]
            if not data:
                continue
            if data[0] == "c" and len(data) >= 1 + len(namespaces):
                source = data[1 + intermediary].replace("/", ".")
                target = data[1 + named].replace("/", ".")
                classes[source] = target
            elif data[0] in {"m", "f"} and len(data) >= 2 + len(namespaces):
                source = data[2 + intermediary]
                target = data[2 + named]
                if source and target and source != target:
                    members[source] = target
    return classes, members


def remap_text(text: str, classes: dict[str, str], members: dict[str, str]) -> str:
    for source in sorted(classes, key=len, reverse=True):
        text = text.replace(source, classes[source])
    simple_classes = {
        source.rsplit(".", 1)[-1]: target.rsplit(".", 1)[-1]
        for source, target in classes.items()
    }
    for source, target in classes.items():
        source_tail = source.rsplit(".", 1)[-1].split("$")
        target_tail = target.rsplit(".", 1)[-1].split("$")
        if len(source_tail) == len(target_tail):
            simple_classes.update(zip(source_tail, target_tail))
    class_pattern = re.compile(r"\bclass_\d+(?:\$class_\d+)*\b")
    text = class_pattern.sub(
        lambda match: simple_classes.get(match.group(0), match.group(0)), text
    )
    token_pattern = re.compile(r"\b(?:method|field)_\d+\b")
    return token_pattern.sub(lambda match: members.get(match.group(0), match.group(0)), text)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("mappings", type=Path)
    parser.add_argument("source", type=Path)
    parser.add_argument("destination", type=Path)
    args = parser.parse_args()

    classes, members = read_tiny(args.mappings)
    if args.destination.exists():
        shutil.rmtree(args.destination)
    shutil.copytree(args.source, args.destination)

    count = 0
    for java_file in args.destination.rglob("*.java"):
        original = java_file.read_text(encoding="utf-8")
        java_file.write_text(remap_text(original, classes, members), encoding="utf-8")
        count += 1
    print(f"Mapped {count} Java files using {len(classes)} classes and {len(members)} members")


if __name__ == "__main__":
    main()
