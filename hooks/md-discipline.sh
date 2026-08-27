#!/bin/bash
# PostToolUse(Write|Edit): 지식 md 수정 시 압축 규칙 리마인더 주입
input=$(cat)
f=$(printf '%s' "$input" | grep -o '"file_path"[[:space:]]*:[[:space:]]*"[^"]*"' | head -1 | sed 's/.*:[[:space:]]*"//; s/"$//')
case "$f" in
  *CLAUDE.md|*AGENTS.md|*MEMORY.md|*memory*.md|*docs*.md)
    printf '%s\n' '{"hookSpecificOutput":{"hookEventName":"PostToolUse","additionalContext":"[md 규율] 늘어진 기록 대신 결론만 남긴다. 제약은 금지와 이유 한 줄씩, 결정은 ADR로 결정, 맥락, 이유, 대가, 재검토 조건, 상태는 덮어쓰기. 원본은 한 곳. 상한은 라우팅 30줄, 상태 15줄, 결정 40줄이고 docs/는 첫 3줄에 요약. 히스토리는 git이 담당하니 끝난 작업 기록은 남길 지식만 옮기고 지운다."}}'
    ;;
esac
exit 0
