package br.com.mybaby;


public class GerenciaConexaoThread implements Runnable {
	
	private BluetoothLeService bluetoothLeService;
	private String address;
	
	GerenciaConexaoThread(BluetoothLeService bluetoothLeService, String address) {
		this.bluetoothLeService = bluetoothLeService;
		this.address = address;
	}
	
	@Override
	public void run() {
		//bluetoothLeService.conectarAssimQueEstiverDisponivel(address);
	}

	
}
