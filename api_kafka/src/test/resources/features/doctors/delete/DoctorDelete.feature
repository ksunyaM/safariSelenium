# DELETE
# The purpose of delete is to delete a resource on the server

Feature: Administrator can delete a user with a role - Doctor, hereinafter referred to as "Doctor"
  As Administrator
  I want to delete a Doctor
  To decrease the list of Doctors in the system

  @RealData
  Scenario: Selected Doctor is deleted properly
    Given The Administrator sets wrong Doctor data
    When The Administrator deletes the Doctor
    Then The Administrator checks that the deleted Doctor is not found

  @RealData
  Scenario: System is not allowed for delete the doctor that was already deleted
    When The Administrator tries to delete the doctor that was already deleted
    Then System informs the Administrator that operation cannot be performed

#  @RealData
#  Scenario: System is not allowed for delete all doctors (404`)
#    Given The Administrator selects all the doctors
#    When The Administrator tries to remove all doctors
#    Then System informs the Administrator about error: 405 Method Not Allowed

#  Scenario: 'No Content' status of deleted Doctor
#    When The Administrator deletes the Doctor
#    Then The Administrator receives '204 No Content' as the response and an empty body (to ensure that DELETE is idempotent) -->Check Error code

#Scenario: The Administrator cannot delete a Doctor with the status "Active"

#Scenario: The Administrator cannot delete a Doctor who has scheduled appointments in the calendar
