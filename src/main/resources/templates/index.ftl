<#import "layout.ftl" as layout>
<#import "component/ui.ftl" as ui>

<@layout.page title="Worde" currentPath="/">
    <#if bannerMessage?? || bannerError??>
        <div class="flash-stack">
            <#if bannerMessage??>
                <div class="flash flash-success">${bannerMessage}</div>
            </#if>
            <#if bannerError??>
                <div class="flash flash-error">${bannerError}</div>
            </#if>
        </div>
    </#if>

    <section class="panel hero-panel">
        <div>
            <h1 class="hero-title">Guess the five-letter word.</h1>
            <p class="hero-copy">Six tries. Letters only. Scores and stats are saved.</p>
        </div>
        <div class="hero-meta">
            <div class="hero-stat">
                <span class="hero-stat-value">${dictionarySize}</span>
                <span class="hero-stat-label">valid guess words</span>
            </div>
            <div class="hero-stat">
                <span class="hero-stat-value">6</span>
                <span class="hero-stat-label">tries per round</span>
            </div>
        </div>
    </section>

    <#if currentUser?? && homeView??>
        <div class="dashboard-grid"> <aside class="panel side-panel">
                <div class="panel-heading compact">
                    <div>
                        <h2>${currentUser.username}</h2>
                        <p>Saved stats.</p>
                    </div>
                </div>

                <div class="stats-grid">
                    <@ui.statCard label="Games" value=currentUser.gamesPlayed meta="Total rounds"/>
                    <@ui.statCard label="Wins" value=currentUser.wins meta="Rounds solved"/>
                    <@ui.statCard label="Win Rate" value=currentUser.winRateLabel meta="Success rate"/>
                    <@ui.statCard label="Best Streak" value=currentUser.bestStreak meta="Highest run"/>
                    <@ui.statCard label="Current Streak" value=currentUser.currentStreak meta="Live streak"/>
                    <@ui.statCard label="Avg. Guesses" value=currentUser.averageWinningGuessesLabel meta="Winning rounds only"/>
                </div>
            </aside>
            <section class="panel board-panel">
                <div class="panel-heading">
                    <div>
                        <h2>Game</h2>
                        <p data-attempts-left>${homeView.attemptsRemaining} tries left</p>
                    </div>
                    <div class="status-group">
                        <#if homeView.finished>
                            <@ui.pill label=(homeView.won?string("Solved", "Missed")) tone=(homeView.won?string("success", "muted"))/>
                        <#else>
                            <@ui.pill label="Playing" tone="accent"/>
                        </#if>
                    </div>
                </div>

                <div class="board-grid" data-board></div>

                <div class="notice-bar <#if homeView.finished>is-open</#if>" data-notice>
                    <#if homeView.finished>
                        <#if homeView.won>
                            Solved. Answer: <strong>${homeView.revealedWord}</strong>.
                        <#else>
                            Missed. Answer: <strong>${homeView.revealedWord}</strong>.
                        </#if>
                    <#else>
                        Enter a five-letter word.
                    </#if>
                </div>

                <#if homeView.finished>
                    <form action="/game/new" method="post" class="guess-form is-finished">
                        <button class="button button-primary" type="submit">New Game</button>
                    </form>
                <#else>
                    <form class="guess-form" data-guess-form>
                        <label class="input-stack" for="guessInput">
                            <span>Guess</span>
                            <input id="guessInput" name="guess" class="guess-input" type="text" maxlength="5" autocomplete="off" spellcheck="false" required>
                        </label>
                        <button class="button button-primary" type="submit" data-submit-button>Submit</button>
                    </form>
                </#if>
            </section>

           

            <aside class="panel side-panel">
                <div class="panel-heading compact">
                    <div>
                        <h2>Leaderboard</h2>
                        <p>Top players.</p>
                    </div>
                </div>
                <@ui.leaderboardTable players=leaderboard currentUserName=currentUser.username/>
                <a class="button button-secondary full-width" href="/leaderboard">View All</a>
            </aside>
        </div>

        <script>
            window.WORDE_STATE = {
                maxAttempts: ${homeView.maxAttempts?c},
                attemptsRemaining: ${homeView.attemptsRemaining?c},
                finished: <#if homeView.finished>true<#else>false</#if>,
                won: <#if homeView.won>true<#else>false</#if>,
                guesses: [
                    <#list homeView.guesses as row>
                    {
                        guess: "${row.guess?js_string}",
                        result: [<#list row.result as value>${value}<#if value_has_next>, </#if></#list>]
                    }<#if row_has_next>,</#if>
                    </#list>
                ]
            };
        </script>
        <script src="/scripts/game.js" defer></script>
    <#else>
        <div class="dashboard-grid is-guest">
            <section class="panel login-panel">
                <div class="panel-heading">
                    <div>
                        <h2>Choose a player name</h2>
                        <p>Use 3 to 24 letters or numbers.</p>
                    </div>
                </div>

                <form action="/session/user" method="post" class="player-form">
                    <label class="input-stack" for="username">
                        <span>Name</span>
                        <input id="username" name="username" class="guess-input" type="text" maxlength="24" autocomplete="off" required>
                    </label>
                    <button class="button button-primary" type="submit">Start</button>
                </form>
            </section>

            <aside class="panel side-panel">
                <div class="panel-heading compact">
                    <div>
                        <h2>Leaderboard</h2>
                        <p>Current players.</p>
                    </div>
                </div>
                <@ui.leaderboardTable players=leaderboard/>
            </aside>
        </div>
    </#if>
</@layout.page>