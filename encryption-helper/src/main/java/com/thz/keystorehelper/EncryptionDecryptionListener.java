package com.thz.keystorehelper;

/**
 * <p></p><b>Description:</b><p></p> Listener interface for calling methods on completion of decryption/encryption
 * <p></p>
 */
public interface EncryptionDecryptionListener {

    /**
     * Called if task is completed successfully and result is delivered in parameter.
     * @param data processed data to be delivered to client (usually Activity or Fragment)
     */
    void onSuccess(String data);


    /**
     * Called if task has encountered any error during operation
     * @param errorMessage error message to be delivered to client (usually Activity or Fragment)
     */
    void onFailure(String errorMessage);
}
