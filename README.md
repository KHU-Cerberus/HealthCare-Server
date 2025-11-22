<h2>📌 HealthCare 프로젝트 커밋 컨벤션</h2>

<p>협업 시 코드 변경 내역을 명확하게 관리하기 위해 <strong>Conventional Commits</strong> 방식을 사용합니다.</p>

<hr>

<h3>1️⃣ 커밋 기본 구조</h3>
<pre>
&lt;type&gt;[optional scope]: &lt;description&gt;
[optional body]
[optional footer(s)]
</pre>

<ul>
  <li><strong>type</strong>: 커밋 종류</li>
  <li><strong>scope</strong> (선택): 영향을 받는 모듈/폴더/파일</li>
  <li><strong>description</strong>: 한 줄 요약</li>
  <li><strong>body</strong> (선택): 상세 설명</li>
  <li><strong>footer</strong> (선택): 이슈 번호, BREAKING CHANGE 등</li>
</ul>

<hr>

<h3>2️⃣ 커밋 타입 예시</h3>
<table>
  <thead>
    <tr>
      <th>Type</th>
      <th>설명</th>
    </tr>
  </thead>
  <tbody>
    <tr><td>feat</td><td>새로운 기능 추가</td></tr>
    <tr><td>fix</td><td>버그 수정</td></tr>
    <tr><td>docs</td><td>문서 변경 (README 등)</td></tr>
    <tr><td>style</td><td>코드 포맷팅, 세미콜론 누락 등 기능 영향 없는 변경</td></tr>
    <tr><td>refactor</td><td>코드 리팩토링 (기능 변화 없음)</td></tr>
    <tr><td>perf</td><td>성능 개선</td></tr>
    <tr><td>test</td><td>테스트 코드 추가/수정</td></tr>
    <tr><td>chore</td><td>빌드, 패키지 매니저 설정 등 잡일</td></tr>
  </tbody>
</table>

<hr>

<h3>3️⃣ 예시 커밋 메시지</h3>
<pre>
feat(auth): JWT 로그인 기능 추가 (#11)
fix(api): /plants GET 요청 오류 수정 (#11)
docs: README에 프로젝트 구조 설명 추가 (#11)
style: 코드 인덴트 정리 (#11)
refactor(user): UserService 메서드 간소화 (#11)
test(plants): 식물 조회 테스트 케이스 추가 (#11)
chore: Gradle 버전 업데이트 (#11)
</pre>

<hr>

<h3>4️⃣ 권장 규칙</h3>
<ul>
  <li>한 줄 설명은 <strong>50자 내외</strong>, 첫 글자는 소문자</li>
  <li>body는 필요하면 <strong>72자 단위로 줄바꿈</strong></li>
  <li>커밋 타입과 scope를 명확히 작성</li>
  <li>이슈 번호와 연동 가능: <code>fix(auth): 로그인 버그 수정 (#12)</code></li>
  <li>팀에서 합의된 언어 사용 (영문 권장)</li>
</ul>

<hr>

<p>✅ 이 가이드를 따르면 프로젝트 히스토리가 명확해지고, 협업 및 릴리즈 관리가 훨씬 쉬워집니다.</p>
