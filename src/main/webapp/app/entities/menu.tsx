import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/driver">
        Driver
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle">
        Vehicle
      </MenuItem>
      <MenuItem icon="asterisk" to="/trip">
        Trip
      </MenuItem>
      <MenuItem icon="asterisk" to="/violation">
        Violation
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
