<#import "layout.ftl" as layout>
<#import "component/ui.ftl" as ui>

<@layout.page title="How Worde Works" currentPath="/about">
    <section class="panel info-panel">
        <div class="panel-heading">
            <div>
                <h1>Rules</h1>
                <p>Short version.</p>
            </div>
        </div>

        <div class="info-grid">
            <article class="info-card">
                <h2>Input</h2>
                <p>Each guess must be exactly five letters. No digits or symbols.</p>
            </article>
            <article class="info-card">
                <h2>Colors</h2>
                <p>Green is correct. Yellow is in the word but misplaced. Gray is not in the word.</p>
            </article>
            <article class="info-card">
                <h2>Secret Word</h2>
                <p>The guess response returns score values only. The answer stays on the server until the round ends.</p>
            </article>
            <article class="info-card">
                <h2>Saved Data</h2>
                <p>Player stats and finished rounds are stored in the database.</p>
            </article>
        </div>
    </section>
</@layout.page>