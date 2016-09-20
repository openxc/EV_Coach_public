/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Android\\StudioProjects\\EV_Coach\\openxc\\src\\main\\aidl\\com\\openxc\\remote\\ViConnectionListener.aidl
 */
package com.openxc.remote;
public interface ViConnectionListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.openxc.remote.ViConnectionListener
{
private static final java.lang.String DESCRIPTOR = "com.openxc.remote.ViConnectionListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.openxc.remote.ViConnectionListener interface,
 * generating a proxy if needed.
 */
public static com.openxc.remote.ViConnectionListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.openxc.remote.ViConnectionListener))) {
return ((com.openxc.remote.ViConnectionListener)iin);
}
return new com.openxc.remote.ViConnectionListener.Stub.Proxy(obj);
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
case TRANSACTION_onConnected:
{
data.enforceInterface(DESCRIPTOR);
com.openxc.interfaces.VehicleInterfaceDescriptor _arg0;
if ((0!=data.readInt())) {
_arg0 = com.openxc.interfaces.VehicleInterfaceDescriptor.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onConnected(_arg0);
return true;
}
case TRANSACTION_onDisconnected:
{
data.enforceInterface(DESCRIPTOR);
this.onDisconnected();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.openxc.remote.ViConnectionListener
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
@Override public void onConnected(com.openxc.interfaces.VehicleInterfaceDescriptor vi) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((vi!=null)) {
_data.writeInt(1);
vi.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onConnected, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onDisconnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onDisconnected, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onDisconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void onConnected(com.openxc.interfaces.VehicleInterfaceDescriptor vi) throws android.os.RemoteException;
public void onDisconnected() throws android.os.RemoteException;
}
