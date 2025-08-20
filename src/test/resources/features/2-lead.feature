@spy
Feature: Lead Cases

  Background:
    Given The user go to 'test-app' environment
    Given The user login
      | username | TestSipay |
      | password | Sipay2025 |

  Scenario: Lead Empty Required Area Control On Submit Form
    When The user select 'Prospect' in deal status filter
    When The user click related record button at row 1
    When The user go to other tab
    Then The user verify 'Lead Formu' form is open
    When The user click 'Tamamla' button
    Then The user verify warning 'Lütfen tüm zorunlu alanları doldurun'

  Scenario: Lead Ürün Selection For Extra Attributes
    When The user select 'Prospect' in deal status filter
    When The user click related record button at row 1
    When The user go to other tab
    Then The user verify 'Lead Formu' form is open
    When The user select 'Manuel POS' in 'product'
    Then The user verify other attributes display
    When The user select 'Linkle Ödeme' in 'product'
    Then The user verify other attributes display
    When The user select 'Sanal POS' in 'product'
    Then The user verify other attributes display
    When The user select 'Fiziki POS' in 'product'
    Then The user verify other attributes display

#  Scenario: Lead Ürün Selection For Extra Attributes 2
#    When The user select 'Prospect' in deal status filter
#    When The user click related record button at row 1
#    When The user go to other tab
#    Then The user verify 'Lead Formu' form is open
#    When The user select 'Linkle Ödeme' in 'product'
#    Then The user verify other attributes display
#
#  Scenario: Lead Ürün Selection For Extra Attributes 3
#    When The user select 'Prospect' in deal status filter
#    When The user click related record button at row 1
#    When The user go to other tab
#    Then The user verify 'Lead Formu' form is open
#    When The user select 'Sanal POS' in 'product'
#    Then The user verify other attributes display
#
#  Scenario: Lead Ürün Selection For Extra Attributes 4
#    When The user select 'Prospect' in deal status filter
#    When The user click related record button at row 1
#    When The user go to other tab
#    Then The user verify 'Lead Formu' form is open
#    When The user select 'Fiziki POS' in 'product'
#    Then The user verify other attributes display

  Scenario: Lead Form Complete Case
    When The user go to 'Prospect' on navbar
    When The user fill the prospect form
    When The user click 'Tamamla' button
    Then The user verify 'Lead Formu' form is open
    When The user select 'Fiziki POS' in 'product'
    When The user fill the lead form
    When The user click 'Tamamla' button
    Then The user verify 'Pitched Kayıt Formu' form is open

  Scenario: Lead Form Clear Button Control
    When The user select 'Prospect' in deal status filter
    When The user click related record button at row 1
    When The user go to other tab
    Then The user verify 'Lead Formu' form is open
    When The user fill the lead form
    When The user click 'Temizle' button
    Then The user verify form inputs clear