@spy
Feature: Search Page Scenarios

  Background:
    Given The user go to 'test-app' environment
    Given The user login
      | username | TestSipay |
      | password | Sipay2025 |

  Scenario: Search Input
    When The user enter 'Sena' to search input
    Then The user verify record titles with 'Sena'

  Scenario: Deal Status Filter 1
    When The user select 'Prospect' in deal status filter
    Then The user verify deal status filter with 'Prospect'

  Scenario: Deal Status Filter 2
    When The user select 'Pitched' in deal status filter
    Then The user verify deal status filter with 'Pitched'

  Scenario: Deal Durumu Reset Filter
    When The user select 'Prospect' in deal status filter
    When The user click deal status reset button
    And The user verify Reset button func for deal status filter

  Scenario: Create Date Sorting
    When The user enter 'Sena' to search input
    When The user click create date sort button
    Then The user verify create date ascending sort
    When The user click create date sort button
    Then The user verify create date descending sort

  Scenario: Create date filter
    When The user enter 'Sena' to search input
    When The user select date filter 14 8 2025
    Then The user verify create date filter 14 8 2025

  Scenario: Create date filter reset button
    When The user enter 'Sena' to search input
    When The user select date filter 14 8 2025
    When The user click create date reset button
    Then The user verify Reset button func for create date filter

  Scenario: Save New Record
    When The user click create new record button
    When The user fill and save the form
    Then The user verify record is created

  Scenario: Go To Related Record button
    When The user select 'Prospect' in deal status filter
    When The user click related record button at row 1
    When The user go to other tab
    Then The user verify 'Lead Formu' form is open

  Scenario: Empty Required Area Control On Submit Form
    When The user click create new record button
    When The user click 'Tamamla' button
    Then The user verify warning 'Lütfen tüm zorunlu alanları doldurun'

  Scenario: Pagination Control
    When The user enter 'test' to search input
    Then The user verify previous page button passive
    Then The user verify next button active
    When The user click next page button
    Then The user verify previous page button active
    When The user go to last page
    Then The user verify next button passive
