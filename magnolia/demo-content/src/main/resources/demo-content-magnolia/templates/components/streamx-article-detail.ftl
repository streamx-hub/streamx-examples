[#assign article = model.article]

<div class="flex justify-between px-4 mx-auto max-w-screen-xl ">
  <article class="mx-auto w-full max-w-2xl format format-sm sm:format-base lg:format-lg format-blue">
    <figure><img width="672" height="448" src="${article.asset.link}" class="rounded-lg mb-8" alt="${article.title}">
    </figure>
    <header class="mb-4 lg:mb-6 not-format">
      <p class="text-base text-gray-500 mb-3">${article.author} &#8226; ${article.jobTitle} &#8226; <time>${article.date}</time></p>
      <h1 class="mb-4 text-3xl font-extrabold leading-tight text-gray-900 lg:mb-6 lg:text-4xl">${article.title}</h1>
    </header>
    ${article.content}
  </article>
</div>