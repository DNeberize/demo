<#import "ui.ftl" as ui>

<header class="site-header panel">
    <div class="brand-block">
        <a class="brand-mark" href="/">W</a>
        <div>
            <div class="brand-title">Worde</div>
            <div class="brand-subtitle">Five-letter word game</div>
        </div>
    </div>

    <nav class="site-nav">
        <@ui.navLink href="/" label="Play" currentPath=currentPath/>
        <@ui.navLink href="/leaderboard" label="Leaderboard" currentPath=currentPath/>
        <@ui.navLink href="/about" label="Rules" currentPath=currentPath/>
    </nav>

    <div class="session-block">
        <#if currentUser??>
            <span class="session-chip">${currentUser.username}</span>
            <form action="/session/logout" method="post">
                <button class="button button-ghost" type="submit">Switch Player</button>
            </form>
        <#else>
            <span class="session-hint">Choose a player name to save stats.</span>
        </#if>
    </div>
</header>