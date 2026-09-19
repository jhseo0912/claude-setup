#!/bin/bash
# ~/.claude에 전역 규칙, hook, 스킬, company 역할 스킬을 설치한다. 기존 CLAUDE.md는 .bak으로 백업.
set -e
cd "$(dirname "$0")"
CLAUDE_DIR="${CLAUDE_DIR:-$HOME/.claude}"

mkdir -p "$CLAUDE_DIR/hooks" "$CLAUDE_DIR/skills/distill" "$CLAUDE_DIR/skills/commit" "$CLAUDE_DIR/skills/wrap" "$CLAUDE_DIR/skills/readme" "$CLAUDE_DIR/skills/release"
[ -f "$CLAUDE_DIR/CLAUDE.md" ] && cp "$CLAUDE_DIR/CLAUDE.md" "$CLAUDE_DIR/CLAUDE.md.bak"
cp CLAUDE.md "$CLAUDE_DIR/CLAUDE.md"
cp hooks/md-discipline.sh "$CLAUDE_DIR/hooks/md-discipline.sh"
cp hooks/style-reminder.sh "$CLAUDE_DIR/hooks/style-reminder.sh"
cp skills/distill/SKILL.md "$CLAUDE_DIR/skills/distill/SKILL.md"
cp skills/commit/SKILL.md "$CLAUDE_DIR/skills/commit/SKILL.md"
cp skills/wrap/SKILL.md "$CLAUDE_DIR/skills/wrap/SKILL.md"
cp skills/readme/SKILL.md "$CLAUDE_DIR/skills/readme/SKILL.md"
cp skills/release/SKILL.md "$CLAUDE_DIR/skills/release/SKILL.md"
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

echo "설치 완료. 새 Claude Code 세션부터 적용된다."
