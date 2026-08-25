#!/bin/bash
# ~/.claude에 전역 규칙·hook·distill 스킬을 설치한다. 기존 CLAUDE.md는 .bak으로 백업.
set -e
cd "$(dirname "$0")"
CLAUDE_DIR="${CLAUDE_DIR:-$HOME/.claude}"

mkdir -p "$CLAUDE_DIR/hooks" "$CLAUDE_DIR/skills/distill"
[ -f "$CLAUDE_DIR/CLAUDE.md" ] && cp "$CLAUDE_DIR/CLAUDE.md" "$CLAUDE_DIR/CLAUDE.md.bak"
cp CLAUDE.md "$CLAUDE_DIR/CLAUDE.md"
cp hooks/md-discipline.sh "$CLAUDE_DIR/hooks/md-discipline.sh"
cp skills/distill/SKILL.md "$CLAUDE_DIR/skills/distill/SKILL.md"
node merge-hooks.js "$CLAUDE_DIR/settings.json"

echo "설치 완료. 새 Claude Code 세션부터 적용된다."
