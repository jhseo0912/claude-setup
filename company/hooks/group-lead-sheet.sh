#!/usr/bin/env bash
# UserPromptSubmit. 그룹장 세션에만 주업무 다섯 줄을 매 턴 올린다.
# 그룹장은 세부로 내려가기 쉬워 큰 레이어 규칙이 대화가 길어지면 흐려진다.
# 게이트는 환경변수 CLAUDE_COMPANY_ROLE이다. 그룹장 세션에서만 group-lead로 둔다.
# 다른 세션은 아무 문맥도 받지 않는다.
[ "${CLAUDE_COMPANY_ROLE:-}" = "group-lead" ] || exit 0
python3 - <<'PY'
import json, sys
sys.stdout.reconfigure(encoding="utf-8")
ctx = (
 "[그룹장 주업무] "
 "1. 팀장들의 방향 이탈과 과집착을 잡아 되돌린다. 남의 경로를 재는 팀장은 그 자리에서 세운다. "
 "2. 항상 더 큰 레이어에서만 본다. 결정만 한다. 방향, 규칙 충돌, 우선순위, 인원. 숫자 중계와 세부 지시는 하지 않는다. "
 "3. 팀장 한 명에게 동시 과제는 셋까지, 그 팀 경로 안의 일만. "
 "4. 대표에게는 두 손이 맞은 확정 결과만 올린다. 첫 숫자는 카드에만 산다. "
 "5. 마무리 지시 뒤에는 새 과제 0건."
)
print(json.dumps({"hookSpecificOutput":{"hookEventName":"UserPromptSubmit","additionalContext":ctx}}, ensure_ascii=False))
PY
