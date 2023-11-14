package com.example.lab3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class MojeView extends View{
	private float[] dane=null;

	public void setDane(float[] dane) {
		this.dane = dane;
	}
	public MojeView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
		Paint p = new Paint();
		p.setARGB(255, 255, 0, 0);
		p.setTextSize(50);
		if(dane!=null)
		{
			canvas.drawText("Azymut = "+dane[0], 200, 100, p);
			canvas.drawText("deviceVector = ["+dane[1]+", "+dane[2]+", "+dane[3]+"]", 200, 150, p);
			canvas.drawText("Kat do [Molo, Gmachu Glownego] = ["+dane[4]+", "+dane[5]+"]", 200, 200, p);
			canvas.drawText("kierunekMolo = ["+dane[6]+", "+dane[7]+", "+dane[8]+"]", 200, 250, p);
			canvas.drawText("kierunekGmach = ["+dane[9]+", "+dane[10]+", "+dane[11]+"]", 200, 300, p);
			canvas.drawText("Kat wektora do [Molo, Gmachu Glownego] = ["+dane[12]+", "+dane[13]+"]", 200, 350, p);
			canvas.drawLine(0, 0, 100*dane[0], 100*dane[0], p);
			p.setStyle(Style.STROKE);
			canvas.drawRect(100, 100, 200, 200, p);
			
		}
	}
	
	

}
