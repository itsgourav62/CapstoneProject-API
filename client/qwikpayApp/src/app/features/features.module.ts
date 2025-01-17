import { NgModule } from "@angular/core";
import { UsersigninComponent } from './user/usersignin/usersignin.component';
import { UsersignupComponent } from './user/usersignup/usersignup.component';
import { FooterComponent } from './shared/footer/footer.component';
import { HeaderComponent } from './shared/header/header.component';

@NgModule({
    declarations:[
              UsersigninComponent,
              UsersignupComponent,
              FooterComponent,
              HeaderComponent
  ],
    imports: [

    ],
    exports: [

    ]
})

export class FeaturesModule { }