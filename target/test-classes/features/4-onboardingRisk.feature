@spy
Feature: Onboarding Risk

  Background:
    Given The user go to 'test-app' environment
    Given The user login
      | username | semasipay  |
      | password | Sipay2025. |

  Scenario: Onboarding Risk Empty Revise Control
    When The user select 'Contracting' in deal status filter
    When The user click related record button at row 0
    When The user go to other tab
    When The user wait 5 second
    Then The user verify 'Onboarding Risk Formu' form is open
    When The user click 'Revize Gönder' button
    Then The user verify warning 'Lütfen revize sebebini seçiniz!'

  Scenario: Onboarding Risk Negative taksit
    When The user select 'Contracting' in deal status filter
    When The user click related record button at row 0
    When The user go to other tab
    When The user wait 5 second
    Then The user verify 'Onboarding Risk Formu' form is open
    When The user enter '-2' to taksit input
    Then The user verify minimum value warning

  Scenario: Onboarding Risk Revise Reason and note
    When The user select 'Contracting' in deal status filter
    When The user click related record button at row 0
    When The user go to other tab
    When The user wait 5 second
    Then The user verify 'Onboarding Risk Formu' form is open
    When The user select 'Yasaklı sektör' in 'reviseReason'
#    When The user enter 'Test notu' into explanation in 'risk'
    When The user click 'Güncelle' button
    When The user wait 2 second
    Then The user verify warning 'Durum OnBoardingRiskDraft olarak güncellendi'
    When The user refresh the page
    Then The user verify revise reason 'Yasaklı sektör' for 'risk'
#    Then The user verify explanation 'Test notu' for 'risk'
