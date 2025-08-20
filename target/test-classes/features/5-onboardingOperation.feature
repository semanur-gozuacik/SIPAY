@spy
Feature: Onboarding Operation

  Background:
    Given The user go to 'test-app' environment
    Given The user login
      | username | operatıon_sema |
      | password | Sipay2025.     |

  Scenario: Onboarding Operation Empty Required Input Control
    When The user select 'OnboardingRisk' in deal status filter
    When The user click related record button at row 0
    When The user go to other tab
    When The user wait 5 second
    Then The user verify 'Onboarding Operation Formu' form is open
    When The user click 'Tamamla' button
    Then The user verify warning 'pleaseFillRequiredFields'

  Scenario: Onboarding Operation Revise Reason and note
    When The user select 'OnboardingRisk' in deal status filter
    When The user click related record button at row 0
    When The user go to other tab
    When The user wait 5 second
    Then The user verify 'Onboarding Operation Formu' form is open
    When The user select 'Yasaklı sektör' in 'reviseReasonOperation'
    When The user enter 'Test notu' into explanation in 'operation'
    When The user click 'Güncelle' button
    Then The user verify warning 'Durum OnBoardingOperationDraft olarak güncellendi'
    When The user refresh the page
    Then The user verify revise reason 'Yasaklı sektör' for 'risk'
    Then The user verify explanation 'Test notu' for 'operation'