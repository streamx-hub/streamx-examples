<div class="px-6 pt-10 lg:px-8">
  <div class="mx-auto max-w-2xl py-32 sm:py-48 lg:py-56">
    <div class="text-center">
      <h1 class="text-4xl font-bold tracking-tight text-gray-900 sm:text-6xl">Lorem ipsum dolor sit amet</h1>
      <p class="mt-6 text-lg leading-8 text-gray-600">Anim aute id magna aliqua ad ad non deserunt sunt. Qui irure qui lorem cupidatat commodo. Elit sunt amet fugiat veniam occaecat fugiat aliqua.</p>
      <div class="mt-10 flex items-center justify-center gap-x-6">
        [#assign blogNode = cmsfn.contentByPath("/blog")]
        [#assign productsNode = cmsfn.contentByPath("/products")]
        <a href="${cmsfn.link(blogNode)}" class="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Read articles</a>
        <a href="${cmsfn.link(productsNode)}" class="text-sm font-semibold leading-6 text-gray-900">All products <span aria-hidden="true">→</span></a>
      </div>
    </div>
  </div>
</div>