import { BaseEntity, User } from './../../shared';

const enum TreeType {
    'SPRUCE',
    'CEDAR',
    'PINE'
}

const enum EventType {
    'OTHER',
    'WEDDING',
    'CHILD_BIRTH'
}

export class Application implements BaseEntity {
    constructor(
        public id?: number,
        public type?: TreeType,
        public plateText?: String,
        public placeLatitude?: number,
        public placeLongitude?: number,
        public seedingDate?: any,
        public event?: EventType,
        public user?: User,
    ) {
    }
}
