import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PlantATreeSharedModule } from '../../shared';
import { PlantATreeAdminModule } from '../../admin/admin.module';
import {
    ApplicationService,
    ApplicationPopupService,
    ApplicationComponent,
    ApplicationDetailComponent,
    ApplicationDialogComponent,
    ApplicationPopupComponent,
    ApplicationDeletePopupComponent,
    ApplicationDeleteDialogComponent,
    applicationRoute,
    applicationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...applicationRoute,
    ...applicationPopupRoute,
];

@NgModule({
    imports: [
        PlantATreeSharedModule,
        PlantATreeAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ApplicationComponent,
        ApplicationDetailComponent,
        ApplicationDialogComponent,
        ApplicationDeleteDialogComponent,
        ApplicationPopupComponent,
        ApplicationDeletePopupComponent,
    ],
    entryComponents: [
        ApplicationComponent,
        ApplicationDialogComponent,
        ApplicationPopupComponent,
        ApplicationDeleteDialogComponent,
        ApplicationDeletePopupComponent,
    ],
    providers: [
        ApplicationService,
        ApplicationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PlantATreeApplicationModule {}
