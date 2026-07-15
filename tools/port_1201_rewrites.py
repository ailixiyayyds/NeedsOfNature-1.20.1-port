#!/usr/bin/env python3
"""Apply safe, mechanical source rewrites needed by Minecraft 1.20.1."""

from pathlib import Path
import argparse


REPLACEMENTS = {
    "Identifier.of(": "new Identifier(",
}


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("source_root", type=Path)
    args = parser.parse_args()
    changed = 0
    for path in args.source_root.rglob("*.java"):
        text = path.read_text(encoding="utf-8")
        updated = text
        for source, target in REPLACEMENTS.items():
            updated = updated.replace(source, target)
        if updated != text:
            path.write_text(updated, encoding="utf-8")
            changed += 1
    print(f"Updated {changed} Java files")


if __name__ == "__main__":
    main()
