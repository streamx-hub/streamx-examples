[#assign articles = model.articles]

<section class="py-6 sm:py-12">
  <div class="container p-6 mx-auto space-y-10">
    <div class="space-y-2 text-center">
      <h2 class="text-3xl font-bold">Blog</h2>
      <p class="text-sm">Qualisque erroribus usu at, duo te agam soluta mucius.</p>
    </div>
    <div class="grid grid-cols-1 gap-x-4 gap-y-8 md:grid-cols-2 xl:grid-cols-4">
        [#list articles as article]
          <article class="relative flex flex-col hover:bg-gray-100 border rounded-lg">
            <img alt="${article.title}" class="object-cover w-full h-52 rounded-t-lg" src="${article.asset.link}">
            <div class="flex flex-col flex-1 p-6">
              <h3 class="flex-1 py-2 text-lg font-semibold leading-snug">
                <a rel="noopener noreferrer" href="blog/${article.nodeName}.html" aria-label="${article.title}">
                  <span aria-hidden="true" class="absolute inset-0"></span>
                  ${article.title}
                </a>
              </h3>
              <div class="flex flex-wrap justify-between pt-3 space-x-2 text-xs">
                <span>${article.author}</span>
                <span>${article.date}</span>
              </div>
            </div>
          </article>
        [/#list]
    </div>
  </div>
</section>