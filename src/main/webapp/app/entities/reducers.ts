import driver from 'app/entities/driver/driver.reducer';
import vehicle from 'app/entities/vehicle/vehicle.reducer';
import trip from 'app/entities/trip/trip.reducer';
import checkin from 'app/entities/checkin/checkin.reducer';
import violation from 'app/entities/violation/violation.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  driver,
  vehicle,
  trip,
  checkin,
  violation,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
