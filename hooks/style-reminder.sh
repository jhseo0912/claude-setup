#!/bin/bash
# UserPromptSubmit: 매 턴 문체 규칙 재주입 (긴 세션에서 시스템 프롬프트 희석 방지)
printf '%s\n' '{"hookSpecificOutput":{"hookEventName":"UserPromptSubmit","additionalContext":"[전역 문체] 줄표(—·–) 금지, 쉼표나 마침표로 끊는다. 두괄식, 개조식. 번역투·AI 상투어 금지. [검색 위임] 코드 검색과 탐색은 Agent 도구, subagent_type caveman:cavecrew-investigator로 위임한다. 사용자가 직접 검색해달라고 해도 위임한다. 예외 없다. 고칠 파일이 아니면 읽기도 위임한다."}}'
exit 0
