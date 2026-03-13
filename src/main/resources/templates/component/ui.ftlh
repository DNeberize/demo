<#macro Button href label variant>
  <a href="${href}" class="btn ${variant?default('link')}" role="button">${label}</a>
</#macro>

<#macro article title content url image="">
  <article class="card">
    <#if image?has_content>
      <img src="${image}" alt="${title} image" class="article-image"/>
    </#if>
    <h2>${title}</h2>
    <p>${content}</p>
    <@Button href="${url}" label="Read More" variant="primary"/>
  </article>
</#macro>