<#macro navLink href label currentPath>
    <a class="nav-link <#if currentPath == href>is-active</#if>" href="${href}">${label}</a>
</#macro>

<#macro pill label tone="neutral">
    <span class="pill pill-${tone}">${label}</span>
</#macro>

<#macro statCard label value meta="">
    <div class="stat-card">
        <div class="stat-label">${label}</div>
        <div class="stat-value">${value}</div>
        <#if meta?has_content>
            <div class="stat-meta">${meta}</div>
        </#if>
    </div>
</#macro>

<#macro leaderboardTable players currentUserName="">
    <div class="leaderboard-list">
        <#if players?has_content>
            <#list players as player>
                <article class="leader-row <#if currentUserName?has_content && player.username == currentUserName>is-current</#if>">
                    <div>
                        <div class="leader-name">${player.username}</div>
                        <div class="leader-meta">${player.wins} wins · ${player.winRateLabel}</div>
                    </div>
                    <div class="leader-score">
                        <span>${player.bestStreak}</span>
                        <small>best streak</small>
                    </div>
                </article>
            </#list>
        <#else>
            <div class="leader-empty">No players yet.</div>
        </#if>
    </div>
</#macro>