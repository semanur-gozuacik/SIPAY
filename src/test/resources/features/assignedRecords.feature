@spy
Feature: Assigned Records Page

  Background:
    Given The user go to 'test-app' environment
    Given The user login
      | username | TestSipay |
      | password | Sipay2025 |

  Scenario: Assigned records navigation and display
    When The user click assigned records tab
    Then The user verify assigned records tab

  Scenario: Assign Not To Me Listing Case
    When The user click create new record button
    When The user select 'Emre Uzun' as salesRep
    When The user fill and save the form
    Given The user logout
    Given The user login
      | username | merveakan  |
      | password | Sipay2025. |
    When The user click assigned records tab
    Then The user verify table no result

  Scenario: Assign To Me Listing Case
    When The user click create new record button
    When The user select 'Merve Akan' as salesRep
    When The user fill and save the form
    Given The user logout
    Given The user login
      | username | merveakan  |
      | password | Sipay2025. |
    When The user click assigned records tab
    Then The user verify table with record

  Scenario: Records Not I Created Listing Case
    When The user click create new record button
    When The user select 'Merve Akan' as salesRep
    When The user fill and save the form
    Given The user logout
    Given The user login
      | username | merveakan  |
      | password | Sipay2025. |
    When The user click assigned records tab
    When The user click assignedOrCreatedCheckbox
    Then The user verify table no result for created case

  Scenario: Records I Created Listing Case
    When The user click create new record button
#    When The user select 'Test Sipay' as salesRep
    When The user fill and save the form
    When The user click assigned records tab
    When The user click assignedOrCreatedCheckbox
    Then The user verify table with record

  Scenario: Assigned Records Deal Status Filter 1
    When The user click assigned records tab
    When The user select 'Prospect' in deal status filter
    Then The user verify deal status filter with 'Prospect'

  Scenario: Assigned Records Create date filter
    When The user click assigned records tab
    When The user select date filter 14 8 2025
    Then The user verify create date filter 14 8 2025

  Scenario: Assigned Records Create date and Deal Status filter
    When The user click assigned records tab
    When The user select 'Lead' in deal status filter
    When The user select date filter 12 8 2025
    Then The user verify create date filter 12 8 2025
    Then The user verify deal status filter with 'Lead'

  Scenario: Assigned Records Create date filter reset button
    When The user click assigned records tab
    When The user enter 'Sena' to search input
    When The user select date filter 14 8 2025
    When The user click create date reset button
    Then The user verify Reset button func for create date filter

  Scenario: Assigned Records Go To Related Record button
    When The user click assigned records tab
    When The user select 'Prospect' in deal status filter
    When The user click related record button at row 1
    Then The user verify 'Lead Formu' form is open

  Scenario: Assigned Records Pagination Control
    When The user click assigned records tab
    When The user enter 'test' to search input
    Then The user verify previous page button passive
    Then The user verify next button active
    When The user click next page button
    Then The user verify previous page button active
    When The user go to last page
    Then The user verify next button passive