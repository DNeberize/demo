<#import "layout.ftlh" as layout>
<#import "component/ui.ftlh" as ui>
<@layout.page title="Posts">
    <div>
        <h1>Posts</h1>
        <div style="display: flex; flex-direction: column; gap: 1rem;" class="posts">
            <@ui.article title="First Post" content="This is the first post." url="/posts/1" image="https://picsum.photos/seed/1/400/240"/>
            <@ui.article title="Second Post" content="This is the second post." url="/posts/2" image="https://picsum.photos/seed/2/400/240"/>
            <@ui.article title="Third Post" content="This is the third post." url="/posts/3"/>
        </div>
    </div>
</@layout.page>
