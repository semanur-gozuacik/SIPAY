@spy
Feature: Prospect Cases

  Background:
    Given The user go to 'test-app' environment
    Given The user login
      | username | TestSipay |
      | password | Sipay2025 |

  Scenario: Prospect Empty Required Area Control On Submit Form
    When The user go to 'Prospect' on navbar
    When The user click 'Tamamla' button
    Then The user verify warning 'Lütfen tüm zorunlu alanları doldurun'

  Scenario: Prospect Partial Empty Required Area Control On Submit Form
    When The user go to 'Prospect' on navbar
    When The user fill the form partially
    When The user click 'Tamamla' button
    Then The user verify warning 'Lütfen tüm zorunlu alanları doldurun'

  Scenario: Prospect Form Complete Case
    When The user go to 'Prospect' on navbar
    When The user fill the prospect form
    When The user click 'Tamamla' button
    Then The user verify warning 'Durum Prospect olarak güncellendi'
    Then The user verify 'Lead Formu' form is open
    When The user open 'Prospect' info in form
    When The user wait 3 second
    Then The user verify old values

  Scenario: Prospect Form Clear Button Control
    When The user go to 'Prospect' on navbar
    When The user fill the prospect form
    When The user click 'Temizle' button
    Then The user verify form inputs clear

  Scenario: Prospect Form Invalid Email And Phone
    When The user go to 'Prospect' on navbar
    When The user fill the prospect form with invalid mail and phone
    When The user click 'Tamamla' button
    Then The user verify warning 'Lütfen tüm zorunlu alanları doldurun'
    Then The user verify invalid email and phone warning

#  Scenario: Prospect Form After Completed Old Values Control
#    When The user go to 'Prospect' on navbar
#    When The user wait 3 second
#    When The user fill the prospect form
#    When The user click 'Tamamla' button
#    Then The user verify 'Lead Formu' form is open
#    When The user open 'Prospect' info in form
#    When The user wait 3 second
#    Then The user verify old values