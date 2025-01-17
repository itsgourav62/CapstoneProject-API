import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-forgotpassword',
  templateUrl: './forgotpassword.component.html',
  styleUrls: ['./forgotpassword.component.css']
})
export class ForgotPasswordComponent {
  forgotPasswordForm: FormGroup;
  resetPasswordForm: FormGroup;
  errorMessage: string = '';
  isEmailVerified: boolean = false; // Toggle between email verification and password reset
  email: string = '';

  constructor(private fb: FormBuilder) {
    // Email verification form
    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

    // Reset password form
    this.resetPasswordForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]]
    }, {
      validator: this.passwordMatchValidator
    });
  }

  // Custom validator to check if password and confirm password match
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    if (password !== confirmPassword) {
      return { passwordMismatch: true };
    }
    return null;
  }

  // Submit email for verification
  onEmailSubmit(): void {
    if (this.forgotPasswordForm.valid) {
      this.email = this.forgotPasswordForm.value.email;

      // Simulate API call to check if email exists
      const emailExistsInDB = true; // Replace with real API call
      if (emailExistsInDB) {
        this.isEmailVerified = true; // Move to password reset phase
        this.errorMessage = '';
      } else {
        this.errorMessage = 'The email address does not exist in our records.';
      }
    } else {
      this.errorMessage = 'Please enter a valid email address.';
    }
  }

  // Submit new password
  onPasswordReset(): void {
    if (this.resetPasswordForm.valid) {
      console.log('Password Reset', this.resetPasswordForm.value);

      // Simulate API call to update the password in the database
      alert('Your password has been successfully reset!');
      this.errorMessage = '';
      // Navigate user to Sign In or other appropriate page
    } else {
      this.errorMessage = 'Please fill out the form correctly.';
    }
  }
}
