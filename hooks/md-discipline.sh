#!/bin/bash
# PostToolUse(Write|Edit): 지식 md 수정 시 압축 규칙 리마인더 주입
input=$(cat)
f=$(printf '%s' "$input" | grep -o '"file_path"[[:space:]]*:[[:space:]]*"[^"]*"' | head -1 | sed 's/.*:[[:space:]]*"//; s/"$//')
case "$f" in
  *CLAUDE.md|*AGENTS.md|*MEMORY.md|*memory*.md|*docs*.md)
    printf '%s\n' '{"hookSpecificOutput":{"hookEventName":"PostToolUse","additionalContext":"[md 규율] 서사 로그 금지 — 결론만: 제약(금지+이유 1줄) / 결정(ADR: 결정·맥락·이유·대가·재검토 조건) / 상태(덮어쓰기). 원본은 한 곳. 상한: 라우팅 30줄·상태 15줄·결정 40줄, docs/는 첫 3줄 요약. 히스토리는 git — 완료된 작업 로그는 증류 후 삭제."}}'
    ;;
esac
exit 0
