import {SITEMAP_PAGES} from "./constants";

describe('Verify StreamX', () => {
  it('Sitemap contains published pages', () => {
    cy.request("http://localhost/sitemap.xml").as("response")
    cy.get("@response").should((response) => {
      expect(response.status).to.eq(200)
      SITEMAP_PAGES.forEach((page) => {
        expect(response.body).contains(page)
      })
    })
  })

  it('Search contains link to product page', () => {
    cy.request("http://localhost/search?query=Containix").as("response")
    cy.get("@response").should((response) => {
      expect(response.status).to.eq(200)
      expect(response.body.items[0]).to.have.property("path", "products/product.html")
    })
  })

  it('Article page is present', () => {
    cy.visit("http://localhost/blog/article.html")
    cy.contains("Title of the post")
  })
})
