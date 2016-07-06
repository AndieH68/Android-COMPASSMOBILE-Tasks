package uk.co.etiltd.bluetooth.service;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class bluethermReport  implements Parcelable
{
    public bluethermReport()
    {

    }

    public boolean isTwoInput;

    public Date receivedTime;

    public double Input1HighLimit;
    public double Input1LowLimit;
    public double Input1Reading;
    public double Input1Trim;
    public String Input1Name;
    public double Input2HighLimit;
    public double Input2LowLimit;
    public double Input2Reading;
    public double Input2Trim;
    public String Input2Name;


    public String serialNumber;
    public double batteryPercentage;
    public String sensor1Type;
    public String sensor2Type;
    public String probeType;
    public Date CalibratedAt;

    public String emissivityValue; //Only used with Infrared probes

    public static bluethermReport getEmptyReading()
    {
        bluethermReport btr = new bluethermReport();
        btr.isTwoInput = false;
        btr.receivedTime = new Date();
        btr.Input1HighLimit = -300.0;
        btr.Input1LowLimit = -300.0;
        btr.Input1Reading = -300.0;
        btr.Input1Trim = 0.0;
        btr.Input1Name = "";
        btr.Input2HighLimit = -300.0;
        btr.Input2LowLimit = -300.0;
        btr.Input2Reading = -300.0;
        btr.Input2Trim = 0.0;
        btr.Input2Name = "";
        btr.serialNumber = "";
        btr.batteryPercentage = 0;
        btr.sensor1Type = "None";
        btr.sensor2Type = "None";
        btr.probeType = "None";
        btr.CalibratedAt = new Date();
        btr.emissivityValue = "communications error";
        return btr;
    }
    public static final Parcelable.Creator<bluethermReport> CREATOR = new Parcelable.Creator<bluethermReport>()
    {
        public bluethermReport createFromParcel(Parcel in)
        {
            return new bluethermReport(in);
        }

        public bluethermReport[] newArray(int size)
        {
            return new bluethermReport[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeDouble(Input1HighLimit);
        dest.writeDouble(Input1LowLimit);
        dest.writeDouble(Input1Reading);
        dest.writeDouble(Input1Trim);
        dest.writeString(Input1Name);
        dest.writeDouble(Input2HighLimit);
        dest.writeDouble(Input2LowLimit);
        dest.writeDouble(Input2Reading);
        dest.writeDouble(Input2Trim);
        dest.writeString(Input2Name);
        dest.writeString(serialNumber);
        dest.writeDouble(batteryPercentage);
        dest.writeString(sensor1Type);
        dest.writeString(sensor2Type);
        dest.writeString(probeType);
        dest.writeByte((byte) (isTwoInput ? 1: 0));
        dest.writeLong(receivedTime.getTime());
        dest.writeLong(CalibratedAt.getTime());

        //This value will only be updated when requested emissivity or setting emissivity with an infrared probe
        dest.writeString(emissivityValue);
    }

    public bluethermReport(Parcel in)
    {
        Input1HighLimit = in.readDouble();
        Input1LowLimit = in.readDouble();
        Input1Reading = in.readDouble();
        Input1Trim = in.readDouble();
        Input1Name = in.readString();
        Input2HighLimit = in.readDouble();
        Input2LowLimit = in.readDouble();
        Input2Reading = in.readDouble();
        Input2Trim = in.readDouble();
        Input2Name = in.readString();
        serialNumber = in.readString();
        batteryPercentage = in.readDouble();
        sensor1Type = in.readString();
        sensor2Type = in.readString();
        probeType = in.readString();
        isTwoInput = in.readByte() == 1;
        receivedTime = new Date();
        receivedTime.setTime(in.readLong());
        CalibratedAt = new Date();
        CalibratedAt.setTime(in.readLong());

        //This value will only be updated when requested emissivity or setting emissivity with an infrared probe
        emissivityValue = in.readString();
    }

}
