
generic configuration AdcDriverC() {

	provides interface Read<uint16_t>;
	
}

implementation {

	components new AdcReadClientC();
	Read = AdcReadClientC;

	components AdcDriverP;
	AdcReadClientC.AdcConfigure -> AdcDriverP;
}
