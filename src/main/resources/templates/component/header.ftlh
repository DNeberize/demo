<#import "ui.ftlh" as ui>

<header class="hero card">
    <div class="header-inner">
        <a class="header-title" href="/">Demo</a>
        <nav>
            <@ui.Button href="/" label="Home" variant="primary"/>
            <@ui.Button href="/posts" label="Posts" variant="info"/>
            <@ui.Button href="/contact" label="Contact" variant="warning"/>
            <@ui.Button href="/about" label="About" variant="secondary"/>
        </nav>
    </div>
</header>