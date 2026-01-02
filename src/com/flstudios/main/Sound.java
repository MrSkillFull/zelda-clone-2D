// importando classe principal
package com.flstudios.main;

// importando bibliotecas java
import java.io.*;
import javax.sound.sampled.*;

/**
 * Gerenciador simples de áudio (SFX e música).
 *
 * Ideia principal:
 * - Carregar o recurso de áudio uma única vez em memória (byte[])
 * - Criar N instâncias de {@link Clip} para evitar cortes quando o mesmo som
 *   for disparado em sequência (ex.: tiros)
 */
public class Sound {

	/**
	 * Wrapper que mantém um pool circular de {@link Clip}.
	 *
	 * Isso permite tocar o mesmo som várias vezes sem "atropelar" a reprodução.
	 */
	public static class Clips{
		public Clip[] clips;
		private int p;
		private int count;
		
		/**
		 * Cria o pool de clips a partir de um buffer carregado do classpath.
		 *
		 * @param buffer áudio em bytes (ou null em caso de falha)
		 * @param count quantidade de clips no pool
		 */
		public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
			if(buffer == null)
				return;
			
			clips = new Clip[count];
			this.count = count;
			
			for(int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
			}
		}
		
		/**
		 * Toca o próximo clip do pool (round-robin), reiniciando do frame 0.
		 */
		public void play() {
			if(clips == null) return;
			clips[p].stop();
			clips[p].setFramePosition(0);
			clips[p].start();
			p++;
			if(p>=count) p = 0;
		}
		
		/**
		 * Executa loop do clip atual.
		 *
		 * Observação: o valor 300 é um loop finito alto (não infinito).
		 */
		public void loop() {
			if(clips == null) return;
			clips[p].loop(300);
		}

	}
	
	// recursos de áudio (carregados do classpath em /res)
	public static Clips music = load("/shodafe8bits.wav",1); // música de fundo
	public static Clips collect = load("/Coletar.wav",1); // coletar item
	public static Clips shoot = load("/shoot.wav",1); // disparo
	public static Clips hit = load("/Hit.wav",1); // acerto
	public static Clips dano = load("/Dano.wav",1); // dano recebido
	
	/**
	 * Carrega um arquivo de áudio do classpath em memória e cria o pool de clips.
	 *
	 * @param name caminho do recurso (ex.: "/shoot.wav")
	 * @param count quantidade de instâncias de Clip a manter
	 */
	private static Clips load(String name,int count) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(name));
			
			byte[] buffer = new byte[1024];
			int read = 0;
			while((read = dis.read(buffer)) >= 0) {
				baos.write(buffer,0,read);
			}
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(data,count);
		}catch(Exception e) {
			// fallback: não quebra o jogo caso o áudio falhe; retorna um objeto "mudo"
			try {
				return new Clips(null,0);
			}catch(Exception ee) {
				return null;
			}
		}
	}
}
