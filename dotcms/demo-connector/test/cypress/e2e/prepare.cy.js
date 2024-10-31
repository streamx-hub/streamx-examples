import { IMAGES, PUBLISHED_PAGES } from "./constants";

describe('Publish dotCMS content', () => {
  // All interfaces except Pages has all elements present in iframe. Cypress needs special handling for that
  const getTableIframe = () => {
    return cy
      .get('dot-iframe iframe#detailFrame')
      .eq(0)
      .its("0.contentDocument.body")
      .should("not.be.empty")
      .then(cy.wrap);
  }

  const getDialogIframe = () => {
    return cy
      .get('dot-dialog iframe#detailFrame')
      .eq(0)
      .its("0.contentDocument.body")
      .should("not.be.empty")
      .then(cy.wrap);
  }

  const findAndPublishResource = (title) => {
    // Context menu on results page is unreliable. Using details view to publish content.

    // Open Details dialog
    getTableIframe().find("table#results_table")
      .should('be.visible')
      .contains(title)
      .click()

    // Use right menu to publish page
    getDialogIframe().contains('.content-edit-actions', 'Publish').click();
    getDialogIframe().contains("Content saved");

    //Close dialog
    cy.get('dot-dialog header button').click();
  }

  beforeEach(() => {
    // Login, cy.session keeps cookies/session storage between tests
    cy.session("admin", () => {
      cy.visit('/dotAdmin/')
      cy.get('[data-testid="userNameInput"]').type("admin@dotcms.com")
      cy.get('[data-testid="password"]').type("admin")
      cy.get('[data-testid="submitButton"]').click()
      cy.url().should('include', '/#/starter')
    })
  })

  it('Publish all images/resources', () => {
    cy.visit("/dotAdmin/#/c/content")
    IMAGES.forEach((image) => {
      findAndPublishResource(image)
    })
  })

  it('Publish pages', () => {
    cy.visit("dotAdmin/#/pages")
    PUBLISHED_PAGES.forEach((page) => {
      // Use context menu to publish the image
      cy.get('[data-testid="pages-listing-panel"]').contains(page).rightclick();
      cy.get('.p-contextmenu').filter(":visible").contains('.p-menuitem-link', "Publish").focus().click({force:true});
      cy.contains("Workflow executed");
    })
  })

  it('Publish blog article', () => {
    cy.visit("dotAdmin/#/c/c_Blog-Article_list")
    findAndPublishResource('Title of the post')
  })


  it('Publish product page', () => {
    cy.visit("dotAdmin/#/c/c_Product_list")
    findAndPublishResource('Containix')
  })
})
