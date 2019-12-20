package com.chaokong.service.impl;

import com.chaokong.pojo.CalibrationData;
import com.chaokong.service.IParseCalibrationService;
import com.chaokong.tool.MyBuffer;
import com.chaokong.tool.Tools;
import com.chaokong.util.Kafka;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ParseCalibrationServiceImpl implements IParseCalibrationService {

	private static Logger calibrationLog = Logger.getLogger("calibrationLog");

	/**
	 * 解析出上传标定文件的数据(HexString) 生成透传内容的数据内容(byte[])
	 *
	 * @param caliHex
	 * @return
	 */
	public CalibrationData parseCalibrationData(String caliHex) {

		calibrationLog.info("接收到标定数据==" + caliHex);
		String[] split = caliHex.split(":");
		String simNo = split[0];
		String messageContent = split[1];
		messageContent = messageContent.replace(",", "");
		messageContent = messageContent.replace("\n", "");
		messageContent = messageContent.replace("\\", "");
		messageContent = messageContent.replace(" ", "");
		byte[] bytes = Tools.HexString2Bytes(messageContent);

		MyBuffer myBuffer = new MyBuffer(bytes);
		// 车牌号[12]
		byte[] car_no = myBuffer.gets(12);
		// 额定载重[4]
		int zaizhong_eding = myBuffer.getInt();
		// 分度值[2]
		short aShort = myBuffer.getShort();
		// 传感器个数[1]
		byte no_sensor = myBuffer.get();
		// 单传感器记录数[2]
		short log_single = myBuffer.getShort();
		// 标定数据集合体
		ArrayList<byte[]> logs = new ArrayList<>();
		int i1 = (no_sensor + 1) * 2;
		for (int i = log_single; i > 0; i--) {
			byte[] gets = myBuffer.gets(i1);
			logs.add(gets);
		}
		// CRC32校验码[4]
		int anInt = myBuffer.getInt();
		MyBuffer buff = new MyBuffer(20 + logs.size() * ((byte[]) logs.get(0)).length * 2);
		buff.put(10);
		buff.put(zaizhong_eding * 100);
		buff.put(no_sensor + 0);
		buff.put(log_single);
		buff.put(new byte[]{0, 0, 0, 0, 0, 0});
		for (byte[] log : logs) {
			buff.put(new byte[]{0, 0});
			buff.put(log[log.length - 2]);
			buff.put(log[log.length - 1]);
			for (int i = 0; i < log.length - 2; i += 2) {
				buff.put(new byte[]{0, 0});
				buff.put(log[i]);
				buff.put(log[i + 1]);
			}
		}
		buff.getBuff().reset();
		CalibrationData caliData = new CalibrationData();
		caliData.setSimNo(simNo);
		caliData.setCaliDataBuf(buff.gets(buff.getlength()));
		return caliData;
	}

	public static void main(String[] args) {
		String caliHex = "012030405066:e88b8f422d3939303039000000003038002d0600c15a5e55de5ac158ca4be4525300005a4b55c85ab458bd4bd95248002d5a3755b25aa758b14bce523c005a5a24559c5a9a58a44bc3523000875a1055865a8d58974bb8522500b459fd55705a80588b4bad521900e159e9555a5a73587e4ba2520e010e59d655445a6658724b975202013b59c2552e5a5958654b8c51f6016859af55185a4c58584b8151eb0195599b55025a3f584c4b7651df01c2598754ec5a31583f4b6b51d301ef597454d65a2458334b6051c8021c596054c05a1758264b5551bc0249594d54aa5a0a58194b4a51b102765939549459fd580d4b3f51a502a35926547e59f058004b34519902d05912546959e357f44b29518e02fd58ff545359d657e74b1e5182032a58eb543d59c957da4b135176035758d8542759bc57ce4b08516b038458c4541159af57c14afd515f03b158b053fb59a157b54af2515403de589d53e5599457a84ae75148040b588953cf5987579b4adc513c0438587653b9597a578f4ad151310465586253a3596d57824ac651250492584f538d596057764abb511904bf583b5377595357694ab0510e04ec582853615946575c4aa5510205195814534b593957504a9a50f7054658005335592c57434a8f50eb057357ed531f591f57364a8450df05a057d953095912572a4a7950d405cd57c652f35904571d4a6e50c805fa57b252dd58f757114a6350bc0627579f52c758ea57044a5850b10654578b52b158dd56f74a4d50a506815778529b58d056eb4a42509a06ae5764528558c356de4a37508e06db5751526f58b656d24a2c50820708573d525958a956c54a215077073557295243589c56b84a16506b07625716522d588f56ac4a0b505f078f570252175882569f4a00505407bc56ef52015875569349f5504807e956db51eb5867568649ea503d081656c851d5585a567949df5031084356b451bf584d566d49d45025087056a151a95840566049c9501a089d568d51935833565449be500e08ca567a517d5826564749b3500208f7566651675819563a49a74ff7092456525151580c562e499c4feb0951563f513b57ff562149914fe0097e562b512557f2561549864fd409ab5618510f57e55608497b4fc809d8560450f957d755fb49704fbd0a0555f150e357ca55ef49654fb10a3255dd50cd57bd55e2495a4fa50a5f55ca50b757b055d6494f4f9a0a8c55b650a157a355c949444f8e0ab955a3508b579655bc49394f830ae6558f5075578955b0492e4f770b13557b505f577c55a349234f6b0b4055685049576f559749184f600b6d555450335762558a490d4f540b9a5541501d5755557d49024f480bc7552d50075748557148f74f3d0bf4551a4ff1573a556448ec4f310c2155064fdb572d555848e14f250c4e54f34fc55720554b48d64f1a0c7b54df4faf5713553e48cb4f0e0ca854cc4f995706553248c04f030cd554b84f8356f9552548b54ef70d0254a44f6d56ec551948aa4eeb0d2f54914f5756df550c489f4ee00d5c547d4f4156d254ff48944ed40d89546a4f2b56c554f348894ec80db654564f1556b854e6487e4ebd0de354434eff56aa54da48734eb10e10542f4ee9569d54cd48684ea60e3d541c4ed3569054c0485d4e9a0e6a54084ebd568354b448524e8e0e9753f54ea7567654a748474e830ec453e14e915669549b483c4e770ef153cd4e7b565c548e48314e6b0f1e53ba4e65564f548148264e600f4b53a64e4f56425475481b4e540f7853934e395635546848104e490fa5537f4e235628545b48054e3d0fd2536c4e0d561b544f47fa4e310fff53584df7560d544247ef4e26102c53454de25600543647e44e1a105953314dcc55f3542947d94e0e1086531e4db655e6541c47ce4e0310b3530a4da055d9541047c34df710e052f64d8a55cc540347b84dec110d52e34d7455bf53f747ad4de0113a52cf4d5e55b253ea47a24dd4116752bc4d4855a553dd47974dc9119452a84d32559853d1478c4dbd11c152954d1c558b53c447814db111ee52814d06557e53b847764da6121b526e4cf0557053ab476b4d9a1248525a4cda5563539e47604d8f127552474cc45556539247554d8312a252334cae55495385474a4d7712cf521f4c98553c5379473f4d6c12fc520c4c82552f536c47344d60132951f84c6c5522535f47294d54135651e54c5655155353471e4d49138351d14c405508534647134d3d13b051be4c2a54fb533a47084d3213dd51aa4c1454ee532d46fd4d26140a51974bfe54e0532046f24d1a143751834be854d3531446e74d0f146451704bd254c6530746dc4d031491515c4bbc54b952fb46d14cf714be51484ba654ac52ee46c64cec14eb51354b90549f52e146bb4ce0151851214b7a549252d546b04cd51545510e4b64548552c846a54cc9157250fa4b4e547852bc469a4cbd159f50e74b38546b52af468f4cb215cc50d34b22545e52a246844ca615f950c04b0c5451529646794c9a162650ac4af654435289466e4c8f165350984ae05436527d46634c83168050854aca5429527046584c7816ad50714ab4541c5263464d4c6c16da505e4a9e540f525746424c601707504a4a885402524a46374c55173450374a7253f5523e462c4c49176150234a5c53e8523146214c3d178e50104a4653db522446164c3217bb4ffc4a3053ce5218460b4c2617e84fe94a1a53c1520b46004c1b18154fd54a0453b451ff45f54c0f18424fc149ee53a651f245ea4c03186f4fae49d8539951e545df4bf8189c4f9a49c2538c51d945d44bec18c94f8749ac537f51cc45c94be018f64f734996537251c045be4bd519234f604980536551b345b34bc919504f4c496a535851a645a84bbe197d4f394954534b519a459d4bb219aa4f25493e533e518d45924ba619d74f1249285331518045874b9b1a044efe491253245174457c4b8f1a314eea48fc5316516745714b831a5e4ed748e65309515b45664b781a8b4ec348d052fc514e455b4b6c1ab84eb048ba52ef514145504b611ae54e9c48a452e2513545454b551b124e89488e52d55128453a4b491b3f4e75487852c8511c452f4b3e1b6c4e62486252bb510f45244b321b994e4e484c52ae510245194b261bc64e3b483652a150f6450e4b1b1bf34e274820529450e945034b0f1c204e13480a528750dd44f84b041c4d4e0047f4527950d044ed4af81c7a4dec47de526c50c344e24aec1ca74dd947c8525f50b744d74ae11cd44dc547b2525250aa44cc4ad51d014db2479c5245509e44c14ac91d2e4d9e47865238509144b64abe1d5b4d8b4771522b508444ab4ab21d884d77475b521e507844a04aa71db54d6447455211506b44954a9b1de24d50472f5204505f448a4a8f1e0f4d3c471951f75052447f4a841e3c4d29470351e9504544744a781e694d1546ed51dc503944694a6c1e964d0246d751cf502c445e4a611ec34cee46c151c2502044534a551ef04cdb46ab51b5501344484a4a1f1d4cc7469551a85006443d4a3e1f4a4cb4467f519b4ffa44324a321f774ca04669518e4fed44274a271fa44c8d465351814fe1441c4a1b1fd14c79463d51744fd444114a0f1ffe4c65462751674fc744064a04202b4c524611515a4fbb43fb49f820584c3e45fb514c4fae43f049ec20854c2b45e5513f4fa243e549e120b24c1745cf51324f9543da49d520df4c0445b951254f8843cf49ca210c4bf045a351184f7c43c449be21394bdd458d510b4f6f43b949b221664bc9457750fe4f6343ae49a721934bb6456150f14f5643a3499b21c0891f1ff7";
		ParseCalibrationServiceImpl pcs = new ParseCalibrationServiceImpl();
		CalibrationData calibrationData = pcs.parseCalibrationData(caliHex);
		System.err.println("simNo: " + calibrationData.getSimNo());
		System.err.println("data: " + Tools.bytes2hex(calibrationData.getCaliDataBuf()));
		String simNo = calibrationData.getSimNo();
		byte[] caliDataBuf = calibrationData.getCaliDataBuf();
		Kafka.producerSendMessage(caliDataBuf, "caliDataDown", ByteArraySerializer.class.getName(), simNo);
	}
}
