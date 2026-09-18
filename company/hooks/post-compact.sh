#!/usr/bin/env bash
# SessionStart(matcher: compact). hookEventName에 "PostCompact"는 없다. 스키마가 거부한다.
# cwd에 company-docs/roster.md가 있을 때만 복구 절차를 주입한다. 다른 프로젝트는 아무 출력 없음.
input=$(cat)
cwd=$(printf '%s' "$input" | grep -o '"cwd"[[:space:]]*:[[:space:]]*"[^"]*"' | head -1 | sed 's/.*:[[:space:]]*"//; s/"$//')
[ -z "$cwd" ] && cwd="$PWD"
cwd=${cwd%/}
[ -f "$cwd/company-docs/roster.md" ] || exit 0
python - "$cwd" <<'PY'
import json, sys
sys.stdout.reconfigure(encoding="utf-8")
cwd = sys.argv[1]
ctx = (
 "[compact 복구 절차] 컨텍스트가 압축됐다. 하던 일을 잇기 전에 순서대로 한다. "
 "1) ListAgents로 이 세션의 이름과 짧은 id를 확인한다. mcp ccd_session_mgmt get_session self로 긴 id도 확인한다. "
 f"2) {cwd}/company-docs/roster.md에서 자기 역할, 상사, 명부에 적힌 주소를 확인한다. "
 "3) CLAUDE.md, company-docs/org.md, 자기 역할 스킬(/role-<역할>)과 company-docs/roles/ 아래 프로젝트 직무 파일을 다시 읽는다. "
 "4) HR(roster의 주소)에게 [PING-SELF] 카드를 보낸다. 내용은 역할, 세션명, 짧은 id, 긴 id, 진행 중 태스크 id, 명부와 다른 점. "
 "5) 그 뒤 하던 태스크를 이어간다. 무엇을 하던 중인지 모호하면 상사에게 [ASK]. "
 "카드 형식은 company-docs/session-protocol.md. 커밋과 git 인덱스는 PM만 만진다."
)
print(json.dumps({"hookSpecificOutput":{"hookEventName":"SessionStart","additionalContext":ctx}}, ensure_ascii=False))
PY
