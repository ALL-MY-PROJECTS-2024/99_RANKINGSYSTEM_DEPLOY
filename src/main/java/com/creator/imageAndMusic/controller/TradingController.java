package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.ChatRoom;
import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.repository.TradingImageRepository;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import com.creator.imageAndMusic.properties.SOCKET;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/trading")
public class TradingController {
   
    /*
        req : 사용자 경매 요청 ->
    */
    @Autowired
    TradingImageServiceImpl tradingImageService;


    @GetMapping("/req")
    public @ResponseBody ResponseEntity<String> req(@RequestParam("fildid") Long fileId, @AuthenticationPrincipal PrincipalDetails principalDetails){

        log.info("GET /trading/req " + fileId);
        TradingImageDto tradeImageDto = new TradingImageDto();
        tradeImageDto.setFileid(fileId);
        tradeImageDto.setSeller(principalDetails.getUsername());
        Map<String,Object> result =  tradingImageService.requestTradingImage(tradeImageDto);

        boolean status = (boolean) result.get("status");
        String message = (String) result.get("message");

        if(!status){
            return new ResponseEntity(message, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity(message, HttpStatus.OK);
    }
    @PostMapping("/my")
    public void my(){
        log.info("GET /trading/my");
    }

    /*
    auction
    */
    @GetMapping("/auction/chat")
    public void auction_chat(){
        log.info("GET /trading/auction/chat");
    }



    /*
        image
    */


    /*
        music
    */


    /*
        calendar
    */
    @GetMapping("/calendar/main")
    public void trading_calendar(Model model){

        log.info("GET /trading/calendar/main");
        List<TradingImage> listEntity =  tradingImageService.getAllTradingImages();

       List<TradingImageDto> list = new ArrayList();

        listEntity.forEach(entity ->{
            TradingImageDto dto = new TradingImageDto();
            dto.setTradingid(entity.getTradingid());
            dto.setTitle( (entity.isAdminAccepted())?"[승인] 경매":"[미승인]경매" );
            dto.setSeller((entity.getSeller()!=null)?entity.getSeller().getUsername():null);
            dto.setBuyer( (entity.getBuyer()!=null)?entity.getBuyer().getUsername():null);
            dto.setFileid(entity.getFileid().getFileid());
            dto.setAdminAccepted(entity.isAdminAccepted());
            dto.setAuctionStartTime(entity.getAuctionStartTime());
            dto.setAuctionEndTime(entity.getAuctionEndTime());
            dto.setPrice(entity.getPrice());
            dto.setPaymentState(entity.isPaymentState());

            //채팅방
            String roomId = entity.getRoomId();


            list.add(dto);
        });

        System.out.println(list);
        model.addAttribute("list",list);

    }

    @GetMapping("/calendar/add")
    public void trading_calendar_add(){
        log.info("GET /trading/calendar/add");
    }
    @GetMapping("/calendar/del")
    public void trading_calendar_del(){
        log.info("GET /trading/calendar/del");
    }
    @GetMapping("/calendar/update")
    public void trading_calendar_update(){
        log.info("GET /trading/calendar/update");
    }


    @GetMapping("/image/main")
    public void image_main(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        log.info("GET /trading/image/main..");
        List<TradingImage> listEntity =  tradingImageService.getAllTradingImages();
        List<TradingImageDto> list = new ArrayList();

        listEntity.forEach(entity ->{
            TradingImageDto dto = new TradingImageDto();
            dto.setTradingid(entity.getTradingid());
            dto.setTitle(entity.getFileid().getImages().getTitle() );


                dto.setSeller((entity.getSeller()!=null)?entity.getSeller().getUsername():null);
                dto.setBuyer( (entity.getBuyer()!=null)?entity.getBuyer().getUsername():null);
                dto.setFileid(entity.getFileid().getFileid());
                dto.setFileDir(entity.getFileid().getDir());
                dto.setFilename(entity.getFileid().getFilename());
                dto.setAdminAccepted(entity.isAdminAccepted());
                dto.setAuctionStartTime(entity.getAuctionStartTime());
                dto.setAuctionEndTime(entity.getAuctionEndTime());
                dto.setPrice(entity.getPrice());
                dto.setPaymentState(entity.isPaymentState());
                dto.setCur(entity.getCur());
                //채팅방
                dto.setRoomId(entity.getRoomId());
                dto.setMax(entity.getMax());
                dto.setMembers(entity.getMembers());

                //요청버튼 활성화여부
                List<String> members = entity.getMembers();

                if(members.isEmpty()){
                    dto.setReq(true);
                }
                else if(dto.getRoomId()==null){
                    dto.setReq(false);
                }
                else if(members.size()>=5){
                    dto.setReq(false);
                }
                else if(members.contains(principalDetails.getUserDto().getUsername())){
                    dto.setReq(false);
                }else{
                    dto.setReq(true);
                }



                //참가목록에 있다면 입장버튼 true
                for(String member : entity.getMembers()){
                    System.out.println(member + "==" +principalDetails.getUserDto().getUsername() +" > "+  member.equals(principalDetails.getUserDto().getUsername()));
                    if(member.equals(principalDetails.getUserDto().getUsername())&&dto.getRoomId()!=null ){
                        dto.setJoin(true);
                        break;
                    }
                }
                String role = principalDetails.getUserDto().getRole();
                if(role.equals("ROLE_ADMIN"))
                    dto.setJoin(true);
            list.add(dto);
        });

        System.out.println(list);
        model.addAttribute("list",list);
        model.addAttribute("username",principalDetails.getUserDto().getUsername());
        ;

    }

    @GetMapping("/image/accept")
    public String trading_accept(TradingImageDto dto){
      log.info("GET /trading/image/accept..." + dto.getTradingid());

      tradingImageService.acceptTradingImages(dto);

      return "redirect:/trading/image/main";
    }

    @GetMapping("/chat/create")        //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(@RequestParam("tradingid") Long tradingid, Model model) {
        log.info("POST /trading/chat/create... name : " + tradingid  );
        tradingImageService.createRoom("이미지 경매 채팅방",tradingid);
        //return "redirect:/trading/chat/room?roomId="+room.getRoomId()+"&username="+username;  //만든사람이 채팅방 1빠로 들어가게 됩니다
        return "redirect:/trading/image/main";
    }
    
    //채팅 관련
    @GetMapping("/chat/list")
    public String chatList(Model model){
        List<ChatRoom> roomList = tradingImageService.findAllRoom();
        model.addAttribute("roomList",roomList);
        return "trading/chat/list";
    }

    @GetMapping("/chat/enter")
    public String chat_room( @RequestParam("roomId") String roomId, @AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        ChatRoom room = tradingImageService.findRoomById(roomId);
        model.addAttribute("room",room);            //현재 방에 들어오기위해서 필요한데...... 접속자 수 등등은 실시간으로 보여줘야 돼서 여기서는 못함

        String username = principalDetails.getUserDto().getUsername();
        model.addAttribute("username",username);
        model.addAttribute("wspath", SOCKET.REQ_PATH);

        return "trading/chat/room";
    }
    @GetMapping("/chat/req")
    public String chat_req(@RequestParam("tradingid") Long tradingid,@AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        String username = principalDetails.getUserDto().getUsername();
        tradingImageService.joinChatMember(tradingid,username);

        return "redirect:/trading/image/main";
    }

}