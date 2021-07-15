package com.devonfw.application.mtsj.general.common.api;

import net.sf.mmm.util.nls.api.NlsBundle;
import net.sf.mmm.util.nls.api.NlsBundleMessage;
import net.sf.mmm.util.nls.api.NlsMessage;

/**
 * This is the {@link NlsBundle} for this application.
 */
public interface NlsBundleApplicationRoot extends NlsBundle {

  /**
   * @see com.test.general.common.api.exception.NoActiveUserException
   *
   * @return the {@link NlsMessage}.
   */
  @NlsBundleMessage("There is currently no user logged in")
  NlsMessage errorNoActiveUser();

  @NlsBundleMessage("The booking can not be cancelled.")
  NlsMessage errorCancelInviteNotAllowed();

  @NlsBundleMessage("The invitation does not exist")
  NlsMessage errorNoInvite();

  @NlsBundleMessage("The order can not be cancelled.")
  NlsMessage errorCancelNotAllowed();

  @NlsBundleMessage("The booking does not exist")
  NlsMessage errorNoBooking();

  @NlsBundleMessage("The order for this booking already exist. Please cancel the order before create a new one.")
  NlsMessage erroOrderAlreadyExist();

  @NlsBundleMessage("Not a valid token")
  NlsMessage errorWrongToken();

}
