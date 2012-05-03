
// add some comments later



//interface MaxBotixRead<val_t> {
interface MaxBotixRead {
  /**
   * Initiates a read of the value.
   * 
   * @return SUCCESS if a readDone() event will eventually come back.
   */
  command error_t read();

  /**
   * Signals the completion of the read().
   *
   * @param result SUCCESS if the read() was successful
   * @param val the value that has been read
   */
  event void readDone( error_t result, uint16_t val );


  /**
   * Initiates a read of 4 consecutive values from the sensor.
   * 
   * @return SUCCESS if a readDone() event will eventually come back.
   */
  command error_t read4();

  /**
   * Signals the completion of the read4().
   *
   * @param result SUCCESS if the read() was successful
   * @param val the value that has been read
   */
  event void read4Done( error_t result, uint16_t val[4] );


  /**
   * Initiates a read of 8 consecutive values from the sensor.
   * 
   * @return SUCCESS if a readDone() event will eventually come back.
   */
  command error_t read8();

  /**
   * Signals the completion of the read8().
   *
   * @param result SUCCESS if the read8() was successful
   * @param val the value that has been read
   */
  event void read8Done( error_t result, uint16_t val[8] );

}
