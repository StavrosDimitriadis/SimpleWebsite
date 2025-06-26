import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function pastOrPresentDate(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {

    if (control.value) {

      const inputDate = new Date(control.value);
      inputDate.setHours(0, 0, 0, 0);

      const today = new Date();
      today.setHours(0, 0, 0, 0);

      return (inputDate > today) ? { futureDate: true } : null;

    } else {
      return null;
    }
  }
}