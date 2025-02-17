package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.dto.TradingMusicDto;
import com.creator.imageAndMusic.domain.entity.ConnectionUser;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.entity.TradingMusic;
import com.creator.imageAndMusic.domain.repository.ConnectionUserRepository;
import com.creator.imageAndMusic.domain.service.SettingServiceImpl;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import com.creator.imageAndMusic.domain.service.TradingMusicServiceImpl;
import com.creator.imageAndMusic.properties.ADMINPROPERTIES;
import com.creator.imageAndMusic.properties.SOCKET;
import com.creator.imageAndMusic.properties.UPLOADPATH;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/admin")
@Tag(name = "관리자 설정", description = "접속 현황,API DOCUMENT(SWAGGER),경매환경설정,업로드환경설정,관리자KEY설정")
public class AdminController {

    @Autowired
    private TradingImageServiceImpl tradingImageService;

    @Autowired
    private TradingMusicServiceImpl tradingMusicService;

    @Autowired
    private SettingServiceImpl settingService;

    @GetMapping("/main")
    public void main(Model model){
        log.info("/GET /admin/main");
    }


    @Operation(
            summary = "홈>TRADING>이미지>메인",
            description = "이미지 매매 페이지 입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/image/main")
    public void trading_main(Model model){
        log.info("/GET /admin/trade/main");
        List<TradingImage> list = tradingImageService.getAllTradingImages();
        model.addAttribute("list" ,list);
    }


    @Operation(
            summary = "홈>TRADING>이미지>승인",
            description = "매매 승인 기능입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/image/accept")
    public String trading_accept(@ModelAttribute TradingImageDto dto, RedirectAttributes rttr){
        log.info("/GET /admin/trade/accept");
        boolean isAccepted = tradingImageService.acceptTradingImages(dto);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인하였습니다");
            return "redirect:/admin/trading/image/main";
        }
        rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인실패..");
        return "redirect:/admin/trading/image/main";

    }

    @Operation(
            summary = "홈>TRADING>이미지>취소",
            description = "매매 취소 기능입니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/image/cancel")
    public String trading_cancel(@RequestParam("tradingid") Long tradingId, RedirectAttributes rttr){
        log.info("/GET /admin/trade/cancel");
        boolean isAccepted = tradingImageService.cancelTradingImages(tradingId);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+tradingId+ "승인 취소하였습니다");
            return "redirect:/admin/trading/image/main";
        }
        rttr.addFlashAttribute("message","id : "+tradingId+ "취소 실패..");
        return "redirect:/admin/trading/image/main";

    }
    //----------------------------------------------------------------
    //음악
    //----------------------------------------------------------------

    @Operation(
            summary = "홈>TRADING>음악>메인",
            description = "음악 매매 페이지 입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/music/main")
    public void trading_main_music(Model model){
        log.info("/GET /admin/trade/main");
        List<TradingMusic> list = tradingMusicService.getAllTradingMusic();
        model.addAttribute("list" ,list);
    }



    @Operation(
            summary = "홈>TRADING>음악>승인",
            description = "음악 매매 승인 기능입니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/music/accept")
    public String trading_accept_music(@ModelAttribute TradingMusicDto dto, RedirectAttributes rttr){
        log.info("/GET /admin/trade/accept");
        boolean isAccepted = tradingMusicService.acceptTradingMusic(dto);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인하였습니다");
            return "redirect:/admin/trading/music/main";
        }
        rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인실패..");
        return "redirect:/admin/trading/music/main";

    }

    @Operation(
            summary = "홈>TRADING>음악>취소",
            description = "음악 매매 취소 기능입니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/music/cancel")
    public String trading_cancel_music(@RequestParam("tradingid") Long tradingId, RedirectAttributes rttr){
        log.info("/GET /admin/trade/cancel");
        boolean isAccepted = tradingMusicService.cancelTradingMusic(tradingId);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+tradingId+ "승인 취소하였습니다");
            return "redirect:/admin/trading/main";
        }
        rttr.addFlashAttribute("message","id : "+tradingId+ "취소 실패..");
        return "redirect:/admin/trading/main";

    }
    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------

    @Autowired
    private ConnectionUserRepository connectionUserRepository;


    @Operation(
            summary = "홈>관리자 설정",
            description = "관리저 설정 페이지 입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/settings")
    public void settings(Model model){

        log.info("GET /admin/settings...");

        Map<String,Object> result = settingService.getAllConnectionList();
        List<ConnectionUser> totalConnectionList = (List<ConnectionUser>)result.get("totalConnectionList");
        model.addAttribute("totalConnectionList",totalConnectionList);

        Map<LocalDate,Object> weeklyConnectionList =  settingService.getConnectionListPerWeekly();
        model.addAttribute("weeklyConnectionList",weeklyConnectionList);


        //최대 접속인원 전달
        model.addAttribute("SOCKETMAX", SOCKET.max);
        model.addAttribute("UPLOADIMAGEMAX", UPLOADPATH.userImageMax);
        model.addAttribute("UPLOADMUSICMAX", UPLOADPATH.userMusicMax);
        model.addAttribute("IMPORT_UID", ADMINPROPERTIES.IMPORT_UID);
        model.addAttribute("ADMIN_APP_KEY", ADMINPROPERTIES.ADMIN_APP_KEY);
        model.addAttribute("ADMIN_EMAIL", ADMINPROPERTIES.ADMIN_EMAIL);


    }

    @GetMapping("/setSocketVal")
    public @ResponseBody ResponseEntity<String> socketVal(@RequestParam("socketMax") Long SocketMax){
        SOCKET.max = SocketMax;
        log.info("GET /admin/setSocketVal..changeSocketVal : " + SOCKET.max);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping("/setImageUploadVal")
    public @ResponseBody ResponseEntity<String> setImageUploadVal(@RequestParam("imageMax") Long imageMax){
        UPLOADPATH.userImageMax = imageMax;
        log.info("GET /admin/setImageUploadVal..UPLOADPATH.userImageMax : " + UPLOADPATH.userImageMax);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
    @GetMapping("/setMusicUploadVal")
    public @ResponseBody ResponseEntity<String> setMusicUploadVal(@RequestParam("musicMax") Long musicMax){
        UPLOADPATH.userMusicMax = musicMax;
        log.info("GET /admin/setImageUploadVal..UPLOADPATH.userMusicMax : " + UPLOADPATH.userMusicMax);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PostMapping("/setAdminMail")
    public @ResponseBody ResponseEntity<String> setAdminMail(
            @RequestParam("adminMail") String adminMail,
            @RequestParam("mailPassword") String mailPassword
    )
    {
        ADMINPROPERTIES.ADMIN_EMAIL = adminMail;
        ADMINPROPERTIES.ADMIN_APP_KEY = mailPassword;
        log.info("POST /admin/setAdminMail..ADMINPROPERTIES.ADMIN_EMAIL : " + ADMINPROPERTIES.ADMIN_EMAIL);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PostMapping("/setPaymentVal")
    public @ResponseBody ResponseEntity<String> setPaymentVal(
            @RequestParam("importUid") String importUid
    )
    {
        ADMINPROPERTIES.IMPORT_UID = importUid;
        log.info("POST /admin/setPaymentVal..ADMINPROPERTIES.IMPORT_UID : " + ADMINPROPERTIES.IMPORT_UID);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }


    @GetMapping("/guide/email")
    public void guideEmail(){

    }
    @GetMapping("/guide/portOne")
    public void guidePortONE(){

    }


}
