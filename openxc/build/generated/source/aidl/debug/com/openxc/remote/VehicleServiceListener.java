/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Android\\StudioProjects\\EV_Coach\\openxc\\src\\main\\aidl\\com\\openxc\\remote\\VehicleServiceListener.aidl
 */
package com.openxc.remote;
/**
 * The interface for receiving a measurement update callback from the
 * VehicleService over AIDL.
 */
public interface VehicleServiceListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.openxc.remote.VehicleServiceListener
{
private static final java.lang.String DESCRIPTOR = "com.openxc.remote.VehicleServiceListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.openxc.remote.VehicleServiceListener interface,
 * generating a proxy if needed.
 */
public static com.openxc.remote.VehicleServiceListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.openxc.remote.VehicleServiceListener))) {
return ((com.openxc.remote.VehicleServiceListener)iin);
}
return new com.openxc.remote.VehicleServiceListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_receive:
{
data.enforceInterface(DESCRIPTOR);
com.openxc.messages.VehicleMessage _arg0;
if ((0!=data.readInt())) {
_arg0 = com.openxc.messages.VehicleMessage.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.receive(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.openxc.remote.VehicleServiceListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void receive(com.openxc.messages.VehicleMessage value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((value!=null)) {
_data.writeInt(1);
value.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_receive, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_receive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void receive(com.openxc.messages.VehicleMessage value) throws android.os.RemoteException;
}
