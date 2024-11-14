<!DOCTYPE html>
<html lang="en">
<head>
    [@cms.page /]
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
    <meta name="description" content="This example presents how to change static html files located on your local into pages available via StreamX">
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/streamx.css">
</head>
<body class="m-0 flex flex-col min-h-screen">
    <header>
        <nav class="bg-white">
            <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
                <div class="relative flex sm:h-16 items-center justify-between">
                    <div class="flex flex-1 items-center justify-center sm:items-center flex-col sm:flex-row sm:justify-between flex-wrap sm:flex-nowrap">
                        [#assign rootNode = cmsfn.contentByPath("/index")]
                        [#assign blogNode = cmsfn.contentByPath("/blog")]
                        [#assign productsNode = cmsfn.contentByPath("/products")]
                        <div class="flex flex-shrink-0 items-center">
                            <a href="${cmsfn.link(rootNode)}" class="px-3 py-2"><span class="text-indigo-600 font-semibold">StreamX </span>&#65372; HTML</a>
                        </div>
                        <div class="sm:ml-6 flex flex-wrap sm:flex-nowrap">
                            <div class="flex space-x-4 flex-wrap sm:flex-nowrap sm:ml-0">
                                <a href="${cmsfn.link(rootNode)}" class="rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-100">Home</a>
                                <a href="${cmsfn.link(blogNode)}" class="rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-100">Blog</a>
                                <a href="${cmsfn.link(productsNode)}" class="rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-100">Products</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    </header>

    <main class="flex-1">
        [@cms.area name="main"/]
    </main>

    <footer>
        <hr class="mt-6 mb-3 border-gray-200 mx-10"/>
        <div class="flex flex-col items-center sm:flex-row sm:justify-center mb-3">
            <p class="text-sm text-gray-500">&copy; 2024 StreamX Demos</p>
        </div>
    </footer>
</body>
</html>
