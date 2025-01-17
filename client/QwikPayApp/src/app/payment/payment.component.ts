import { Component } from '@angular/core';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent {

  submitPayment() {
    // Implement payment submission logic here
    alert('Payment Submitted!');
  }

  // Toggle between card and UPI details based on selection
  togglePaymentMethod(event: any) {
    const paymentMethod = event.target.value;
    if (paymentMethod === 'card') {
      document.getElementById('cardDetails')!.style.display = 'block';
      document.getElementById('upiDetails')!.style.display = 'none';
    } else {
      document.getElementById('cardDetails')!.style.display = 'none';
      document.getElementById('upiDetails')!.style.display = 'block';
    }
  }

  ngOnInit() {
    const paymentMethodRadios = document.getElementsByName('paymentMethod');
    paymentMethodRadios.forEach((radio: any) => {
      radio.addEventListener('change', this.togglePaymentMethod.bind(this));
    });
  }
}
