/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

/**
 *
 * @author ironman
 */
public class TokenTest {
    
    public static void main(String[] args) {
        String token = Token.generateToken(10);
        System.out.println(token);
        token = Token.generateToken(20);
        System.out.println(token);
    }
}
