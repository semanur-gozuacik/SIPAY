@spy
Feature: contract form

  Background:
    Given The user go to 'test-app' environment
    Given The user login
      | username | TestSipay |
      | password | Sipay2025 |

  Scenario: No Document Warning Control
    When The user select 'Pitched' in deal status filter
    When The user click related record button at row 0
    When The user go to other tab
    Then The user verify 'Contracting Kayıt Formu' form is open
    When The user select 'Limited' in 'companyType'
    When The user select '7' in 'valorSelect'
    When The user click 'Tamamla' button
    Then The user verify warning 'Zorunlu Dökümanları Ekleyiniz: Kimlik, Üye İşyeri Bilgi Formu, Oran Şablonu'

  Scenario: Required Document Control
    When The user go to 'Kontrat' on navbar
    Then The user verify 'Contracting Kayıt Formu' form is open
    Then The user verify required documents

#  Scenario: One Missing Document Control
#    When The user select 'Lead' in deal status filter
#    When The user click related record button at row 0
#    When The user go to other tab
##    When The user go to 'Prospect' on navbar
##    When The user fill the prospect form
##    When The user click 'Tamamla' button
##    Then The user verify 'Lead Formu' form is open
##    When The user select 'Fiziki POS' in 'product'
##    When The user fill the lead form
##    When The user click 'Tamamla' button
##    When The user wait 2 second
#    Then The user verify 'Pitched Kayıt Formu' form is open
#    When The user select 'Approved' in 'offerStatus'
#    When The user select 'Aktif' in 'foreignCurrencyPos'
#    When The user select 20 8 2025 in 'offerDate'
#    When The user click 'Tamamla' button
#    When The user wait 7 second
#    Then The user verify 'Contracting Kayıt Formu' form is open
#    When The user select '7' in 'valorSelect'
#    When The user select 'Adi Ortaklık' in 'companyType'
#    When The user upload documents
#    When The user click 'Tamamla' button
#    Then The user verify warning 'Durum Contracting olarak güncellendi'
#    When The user wait 7 second
#    Then The user verify 'Onboarding Risk Formu' form is open


  Scenario: Contract Complete
    When The user select 'Pitched' in deal status filter
    When The user click related record button at row 0
    When The user go to other tab
    Then The user verify 'Contracting Kayıt Formu' form is open
    When The user select '7' in 'valorSelect'
    When The user select 'Adi Ortaklık' in 'companyType'
    When The user upload documents
    When The user click 'Tamamla' button
#    Then The user verify warning 'Durum Contracting olarak güncellendi'
    When The user wait 7 second
    Then The user verify 'Onboarding Risk Formu' form is open

