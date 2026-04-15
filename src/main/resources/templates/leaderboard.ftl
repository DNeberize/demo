<#import "layout.ftl" as layout>
<#import "component/ui.ftl" as ui>

<@layout.page title="Leaderboard" currentPath="/leaderboard">
    <section class="panel leaderboard-page">
        <div class="panel-heading">
            <div>
                <h1>Leaderboard</h1>
                <p>Sorted by wins and streak.</p>
            </div>
        </div>

        <div class="leaderboard-table">
            <div class="table-head">
                <span>Player</span>
                <span>Wins</span>
                <span>Games</span>
                <span>Win Rate</span>
                <span>Best Streak</span>
                <span>Avg. Guesses</span>
            </div>
            <#if leaderboard?has_content>
                <#list leaderboard as player>
                    <div class="table-row <#if currentUser?? && currentUser.username == player.username>is-current</#if>">
                        <span>${player.username}</span>
                        <span>${player.wins}</span>
                        <span>${player.gamesPlayed}</span>
                        <span>${player.winRateLabel}</span>
                        <span>${player.bestStreak}</span>
                        <span>${player.averageWinningGuessesLabel}</span>
                    </div>
                </#list>
            <#else>
                <div class="leader-empty wide">No players yet.</div>
            </#if>
        </div>
    </section>
</@layout.page>