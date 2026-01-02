// importando classe de mundo
package com.flstudios.world;

/**
 * Câmera do jogo.
 *
 * Mantém o offset (x,y) usado na renderização para converter coordenadas do mundo
 * em coordenadas da tela. O clamp evita que a câmera saia dos limites do mapa.
 */
public class Camera {

	// offset da câmera em pixels
	public static int x, y;
	
	/**
	 * Limita um valor dentro de um intervalo.
	 *
	 * @param Atual valor atual
	 * @param Min limite mínimo
	 * @param Max limite máximo
	 * @return valor clampado entre Min e Max
	 */
	public static int clamp(int Atual, int Min, int Max) {
		if(Atual < Min) {
			Atual = Min;
		}
		if(Atual > Max) {
			Atual = Max;
		}
		return Atual;
	}
}