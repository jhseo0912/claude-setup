#!/bin/bash
# ~/.claude에 전역 규칙, hook, 스킬, company 역할 스킬을 설치한다. 기존 CLAUDE.md는 .bak으로 백업.
set -e
cd "$(dirname "$0")"
CLAUDE_DIR="${CLAUDE_DIR:-$HOME/.claude}"

mkdir -p "$CLAUDE_DIR/hooks" "$CLAUDE_DIR/skills/distill" "$CLAUDE_DIR/skills/commit" "$CLAUDE_DIR/skills/wrap" "$CLAUDE_DIR/skills/readme" "$CLAUDE_DIR/skills/release" "$CLAUDE_DIR/skills/night-run"
[ -f "$CLAUDE_DIR/CLAUDE.md" ] && cp "$CLAUDE_DIR/CLAUDE.md" "$CLAUDE_DIR/CLAUDE.md.bak"
cp CLAUDE.md "$CLAUDE_DIR/CLAUDE.md"
cp hooks/md-discipline.sh "$CLAUDE_DIR/hooks/md-discipline.sh"
cp hooks/style-reminder.sh "$CLAUDE_DIR/hooks/style-reminder.sh"
cp skills/distill/SKILL.md "$CLAUDE_DIR/skills/distill/SKILL.md"
cp skills/commit/SKILL.md "$CLAUDE_DIR/skills/commit/SKILL.md"
cp skills/wrap/SKILL.md "$CLAUDE_DIR/skills/wrap/SKILL.md"
cp skills/readme/SKILL.md "$CLAUDE_DIR/skills/readme/SKILL.md"
cp skills/release/SKILL.md "$CLAUDE_DIR/skills/release/SKILL.md"
cp skills/night-run/SKILL.md "$CLAUDE_DIR/skills/night-run/SKILL.md"
for d in company/skills/*/; do
  n=$(basename "$d"); mkdir -p "$CLAUDE_DIR/skills/$n"; cp -r "$d"/. "$CLAUDE_DIR/skills/$n/"
done
cp company/hooks/post-compact.sh "$CLAUDE_DIR/hooks/post-compact.sh"
cp company/hooks/group-lead-sheet.sh "$CLAUDE_DIR/hooks/group-lead-sheet.sh"
node merge-hooks.js "$CLAUDE_DIR/settings.json"

# humanize-korean(im-not-ai, MIT): 한국어 AI 문체 교정 스킬.
# 정본은 upstream이라 파일을 복사하지 않고 클론해 그쪽 install.sh에 맡긴다.
# git이나 네트워크가 없으면 건너뛴다. 나머지 설치는 그대로 끝난다.
VENDOR="$CLAUDE_DIR/vendor/im-not-ai"
if command -v git >/dev/null 2>&1; then
  if [ -d "$VENDOR/.git" ]; then
    git -C "$VENDOR" pull --quiet --ff-only || echo "im-not-ai 갱신 실패, 기존 버전 유지"
  else
    mkdir -p "$(dirname "$VENDOR")"
    git clone --depth 1 --quiet https://github.com/epoko77-ai/im-not-ai.git "$VENDOR" || true
  fi
  if [ -f "$VENDOR/install.sh" ]; then
    CLAUDE_HOME="$CLAUDE_DIR" bash "$VENDOR/install.sh" --claude-only >/dev/null 2>&1 \
      && echo "humanize-korean 설치 완료" || echo "humanize-korean 설치 실패, 건너뜀"
  else
    echo "im-not-ai 내려받기 실패, humanize-korean 건너뜀"
  fi
else
  echo "git 없음, humanize-korean 건너뜀"
fi

# find-skills(vercel-labs/skills, MIT): 필요한 스킬을 찾아 설치까지 안내하는 스킬.
# skills CLI가 ~/.claude/skills/에 깐다. CLI는 HOME 기준이라 CLAUDE_DIR이 <집>/.claude일 때만 돈다.
# npx가 없거나 node가 22.20보다 낮으면 건너뛴다. 나머지 설치는 그대로 끝난다.
if [ "$(basename "$CLAUDE_DIR")" != ".claude" ]; then
  echo "CLAUDE_DIR이 <집>/.claude가 아님, find-skills 건너뜀"
elif command -v npx >/dev/null 2>&1 \
   && node -e 'const[a,b]=process.versions.node.split(".").map(Number);process.exit(a>22||(a===22&&b>=20)?0:1)' 2>/dev/null; then
  HOME="$(dirname "$CLAUDE_DIR")" npx -y skills add vercel-labs/skills -s find-skills -g -a claude-code -y >/dev/null 2>&1 \
    && echo "find-skills 설치 완료" || echo "find-skills 설치 실패, 건너뜀"
else
  echo "npx 없음 또는 node 22.20 미만, find-skills 건너뜀"
fi

echo "설치 완료. 새 Claude Code 세션부터 적용된다."
