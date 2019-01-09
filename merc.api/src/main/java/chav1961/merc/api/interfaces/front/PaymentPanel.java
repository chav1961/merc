package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercContentException;

public interface PaymentPanel {
	PaymentPanel operationPayment(float sub) throws MercContentException;
}
